/*
 * Copyright (c) 2013 Data Harmonisation Panel
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package eu.esdihumboldt.hale.server.api.controller;

import java.lang.annotation.Annotation
import java.lang.reflect.Method
import java.util.Map.Entry

import javax.servlet.http.HttpServletRequest
import javax.xml.bind.Binder
import javax.xml.bind.JAXBContext
import javax.xml.namespace.QName
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import org.w3c.dom.Document
import org.w3c.dom.Node

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

import de.cs3d.util.logging.ALogger
import de.cs3d.util.logging.ALoggerFactory
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlApplication
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlDoc
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlMethod
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlParam
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlParamStyle
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlRepresentation
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlRequest
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlResource
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlResources
import eu.esdihumboldt.hale.server.api.internal.wadl.generated.WadlResponse

/**
 * Controller generating WADL and documentation.
 * 
 * @author Simon Templer
 */
@Controller
@RequestMapping
class WADL {

	private static final ALogger log = ALoggerFactory.getLogger(WADL)

	// autowired
	private final RequestMappingHandlerMapping handlerMapping

	/**
	 * Constructor for initializing the WADL Controller.
	 * 
	 * @param handlerMapping the handler mapping
	 */
	@Autowired
	WADL(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping
	}

	/**
	 * Generate the API documentation based on the WADL.
	 * 
	 * @param request the HTTP servlet request
	 * @return the documentation view and XML to transform using XSLT
	 * @throws Exception if generating the documentation fails
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/xhtml+xml")
	public ModelAndView getDocumentation(HttpServletRequest request) throws Exception {
		WadlApplication wadl = generateWadl(request)

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance()
		final DocumentBuilder builder = factory.newDocumentBuilder()
		final Document doc = builder.getDOMImplementation().createDocument(null, null, null)

		JAXBContext jaxbContext = JAXBContext
				.newInstance("eu.esdihumboldt.hale.server.api.internal.wadl.generated")
		final Binder<Node> binder = jaxbContext.createBinder()
		binder.marshal(wadl, doc)

		new ModelAndView("doc", "xml", doc)
	}

	/**
	 * Generate WADL based on the available controllers.
	 * 
	 * @param request the HTTP servlet request
	 * @return the WADL object representation that will be converted to XML
	 */
	@RequestMapping(value = "/application.wadl", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody
	WadlApplication generateWadl(HttpServletRequest request) {
		// build the WADL application
		WadlApplication result = new WadlApplication();
		WadlDoc doc = new WadlDoc();
		doc.title = "REST Service WADL"
		result.doc << doc
		WadlResources wadResources = new WadlResources();
		wadResources.base = Main.getBaseUrl(request) + '/';

		// organize handler mappings by patterns
		Multimap resourceHandlers = HashMultimap.create()
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.handlerMethods
		for (Entry<RequestMappingInfo, HandlerMethod> entry in handlerMethods.entrySet()) {
			def patterns = entry.key.patternsCondition.patterns
			patterns.each {
				resourceHandlers.put(it, entry)
			}
		}

		// create resources from handler mappings
		for (String pattern in resourceHandlers.keySet()) {
			WadlResource wadlResource = new WadlResource();

			// resource path
			wadlResource.path = pattern.substring(1)

			//TODO template parameters (PathVariable)

			// organize handler methods by HTTP method
			Multimap methods = HashMultimap.create()
			for (Entry<RequestMappingInfo, HandlerMethod> entry in resourceHandlers.get(pattern)) {
				entry.key.methodsCondition.methods.each {
					methods.put(it, entry)
				}
			}

			for (RequestMethod httpMethod in methods.keySet()) {
				WadlMethod wadlMethod = new WadlMethod();
				wadlMethod.name = httpMethod.name()

				Collection<Entry<RequestMappingInfo, HandlerMethod>> mappedMethods = methods.get(httpMethod)

				// collect handler methods
				//def methodHandlers = mappedMethods.collect { it.value }

				//TODO method documentation

				//TODO method request
				wadlMethod.request = generateRequest(mappedMethods) ?: null

				//TODO method responses
				for (Entry<RequestMappingInfo, HandlerMethod> entry in mappedMethods) {
					/*
					 * XXX what about if there are multiple handler methods w/
					 * only different representations?
					 */
					generateResponse(entry.key, entry.value).each { wadlMethod.response << it }
				}

				wadlResource.methodOrResource << wadlMethod
			}

			wadResources.resource << wadlResource
		}

		result.resources << wadResources
		return result;
	}

	private WadlRequest generateRequest(Collection<Entry<RequestMappingInfo, HandlerMethod>> methods) {
		WadlRequest wadlRequest = new WadlRequest()

		//XXX for now assume the same request for all methods
		def firstEntry = methods.find{ true }
		RequestMappingInfo mappingInfo = firstEntry.key
		HandlerMethod handlerMethod = firstEntry.value

		Method javaMethod = handlerMethod.method

		Annotation[][] annotations = javaMethod.parameterAnnotations
		Class<?>[] paramTypes = javaMethod.parameterTypes
		int parameterCounter = 0;

		for (Annotation[] annotationArr in annotations) {
			for (Annotation annotation : annotationArr) {
				if (annotation instanceof RequestParam) {
					// translate request parameter to WADL query parameter
					RequestParam param = (RequestParam) annotation;

					WadlParam waldParam = new WadlParam();

					waldParam.name = param.value()
					waldParam.style = WadlParamStyle.QUERY
					waldParam.required = param.required()

					parameterCounter = skipDefaultParams(paramTypes, parameterCount)
					if (paramTypes.length > parameterCounter) {
						Class paramType = paramTypes[parameterCounter]
						//XXX do something with param type?
					}

					String defaultValue = cleanDefault(param.defaultValue())
					if (defaultValue) {
						waldParam.setDefault(defaultValue)
					}
					wadlRequest.param << waldParam
				}
				else if (annotation instanceof PathVariable) {
					//FIXME path variables should instead be treated in resource

					PathVariable param = (PathVariable) annotation;

					WadlParam waldParam = new WadlParam();
					waldParam.name = param.value()
					waldParam.style = WadlParamStyle.TEMPLATE
					waldParam.required = true

					parameterCounter = skipDefaultParams(paramTypes, parameterCounter)
					if (paramTypes.length > parameterCounter) {
						Class paramType = paramTypes[parameterCounter]
						//XXX do something with param type?
					}

					wadlRequest.param << waldParam
				}

				parameterCounter++;
			}
		}
		if (wadlRequest.param || wadlRequest.doc) {
			return wadlRequest
		}

		[]
	}

	@SuppressWarnings("rawtypes")
	private int skipDefaultParams(Class[] paramTypes, int currentIndex) {
		if (paramTypes && paramTypes.length > currentIndex) {
			if (paramTypes.length > currentIndex
			&& (paramTypes[currentIndex] == javax.servlet.http.HttpServletRequest.class || paramTypes[currentIndex] == javax.servlet.http.HttpServletResponse.class))
				currentIndex++;
			if (paramTypes.length > currentIndex
			&& (paramTypes[currentIndex] == javax.servlet.http.HttpServletRequest.class || paramTypes[currentIndex] == javax.servlet.http.HttpServletResponse.class))
				currentIndex++;
		}

		currentIndex
	}

	/**
	 * Generate WADL responses.
	 * 
	 * @param mappingInfo the mapping information of a handler method
	 * @param method the handler method
	 * @return the {@link WadlResponse} or an empty list
	 */
	private def generateResponse(RequestMappingInfo mappingInfo, HandlerMethod method) {
		Set<MediaType> mediaTypes = mappingInfo.producesCondition.producibleMediaTypes

		if (mediaTypes) {
			WadlResponse wadlResponse = new WadlResponse()

			//TODO response status codes
			wadlResponse.status << 200l;

			//TODO response documentation from handlerMethod

			for (MediaType mediaType in mediaTypes) {
				WadlRepresentation wadlRepresentation = new WadlRepresentation()
				wadlRepresentation.mediaType = mediaType.toString()

				wadlResponse.representation << wadlRepresentation
			}

			return wadlResponse
		}

		return []
	}

	private String cleanDefault(String value) {
		value = value.replaceAll("\t", "");
		value = value.replaceAll("\n", "");
		value = value.replaceAll("?", "");
		value = value.replaceAll("?", "");
		value = value.replaceAll("?", "");
		return value;
	}

	/**
	 * Determine the XML Schema type name representing a Java class.
	 * 
	 * @param classType the Java class
	 * @return the corresponding schema type or <code>null</code> if none was
	 *         recognized
	 */
	private QName getQNameForType(Class<?> classType) {
		QName qName = null;

		// extract the component type from an array
		if (classType.isArray()) {
			classType = classType.getComponentType();
		}

		if (classType == java.lang.Long.class)
			qName = new QName("http://www.w3.org/2001/XMLSchema", "long");
		else if (classType == java.lang.Integer.class)
			qName = new QName("http://www.w3.org/2001/XMLSchema", "integer");
		else if (classType == java.lang.Double.class)
			qName = new QName("http://www.w3.org/2001/XMLSchema", "double");
		else if (classType == java.lang.String.class)
			qName = new QName("http://www.w3.org/2001/XMLSchema", "string");
		else if (classType == java.util.Date.class)
			qName = new QName("http://www.w3.org/2001/XMLSchema", "date");

		return qName;
	}

}
