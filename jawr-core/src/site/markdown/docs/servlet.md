The Jawr servlet
----------------

For each type of supported resource (CSS, Javascript, Binary \[image,
font, ...\]), you need an instance of net.jawr.web.servlet.JawrServlet
running. Of course, you may choose to use Jawr for only one of these
types of resources.  

By default, all bundles are created and postprocessed when this servlet
starts up and stored at the application server's temporary directory.
This way there is no processing overhead on requests, but you must
remember to use the load-on-startup tag to force the Jawr servlet to
initialize with your application server.  

For every request, the servlet will make an attempt at forcing the
client to keep a resource in cache for as long as possible using
response headers. Also, if a request contains the If-Modified-Since
header, the response status is set to 304(not modified) and no content
is sent since such header means that the resource is already cached by
the client.  

Here is an example configuration of the servlet in a web.xml file:


            <servlet>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.properties</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
            </servlet>
            

We would use this servlet to manage javascript bundles. The init params
for a Jawr servlet are the following:

-   **configLocation**: This is a mandatory parameter. Its value points
    to a classpath resource that Jawr will use to configure itself. See
    the [config file syntax page](./descriptor_syntax.html) for
    more information. You can use the same properties file for both
    instances of the servlet. Optionally, you can point to an external
    file instead of using the classpath. To do that, prefix the file
    path with **file:**. For example: file:C:\\config\\jawr.properties.
-   **type**: Refers to the type of resource the servlet will manage. It
    can receive three possible values: *js* , *css* or *binary*. You can
    omit this parameter for the javascript servlet, since *js* is the
    default value.\
    The *img* value is deprecated and should be replaced by *binary*.  
-   **configPropertiesSourceClass**: This optional parameter holds the
    qualified class name of you custom implementation of
    ConfigPropertiesSource, in case you want to customize the way
    configuration is loaded when Jawr starts up. More on this below. By
    default, Jawr use a properties file as configuration file but it
    also allow users to use a JSON file for configuration using a
    *net.jawr.web.resource.bundle.factory.util.JsonPropertiesSource* class.
    You'll find more info below.
-   **configPropertyResolverClass**: This optional parameter holds the
    qualified class name of your custom implementation of
    ConfigPropertyResolver, in case you want to customize the way
    configuration properties are resolved. More on this below.
-   **mapping**: In normal situations, you will have the Jawr servlet
    attend all requests for a certain type of resource, using a
    servlet-mapping like this:


                
                <servlet-mapping>
                        <servlet-name>JavascriptServlet</servlet-name>
                        <url-pattern>*.js</url-pattern>
                </servlet-mapping> 
                

   However, you might be forced to serve some of your scripts in the
    traditional way. For instance, you might be using a tag library that
    imports scripts on its own, or simply you want to update an existing
    application to use Jawr in some cases, but you don't want to change
    each and every page. You can then use a mapping in the form of
    /jsJawrPath/\*, so that all URLs created by the tag library start
    with this path mapping. You will then need to specify the
    **mapping** init param to let Jawr know about this so that URLs are
    properly formed. So if you map the servlet to /jsJawrPath/\*, the
    mapping init param should have a value of /jsJawrPath/. The
    resulting configuration would end up like this:


                ...
                <servlet>
                        <servlet-name>JavascriptServlet</servlet-name>
                        <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                        <init-param>
                                <param-name>configLocation</param-name>
                                <param-value>/jawr.properties</param-value>
                        </init-param>
                        <init-param>
                                <param-name>mapping</param-name>
                                <param-value>/jsJawrPath/</param-value>
                        </init-param>
                        <load-on-startup>1</load-on-startup>
                </servlet>
                ...
                
                <servlet-mapping>
                        <servlet-name>JavascriptServlet</servlet-name>
                        <url-pattern>/jsJawrPath/*</url-pattern>
                </servlet-mapping> 
                


### Jawr binary resource (image, font, ...) handling

Jawr supports binary web resources (images, fonts, ...). To enable this
feature, you will have to define the Jawr binary servlet in your web.xml
file. When the Jawr binary servlet is defined in you application, all
CSS images or fonts will be handled by this servlet.

In order for Jawr to properly handle the CSS images and fonts, the Jawr
Binary servlet must start before the Jawr CSS servlet. This means that
you will have to set properly the **load.on.startup** property in your
web.xml like below :

            ...
            <servlet>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.properties</param-value>
                    </init-param>
                    <init-param>
                            <param-name>type</param-name>
                            <param-value>binary</param-value>
                    </init-param>
                    <!-- The Jawr Binary servlet must start before the Jawr CSS Servlet -->
                    <load-on-startup>0</load-on-startup>
            </servlet>
            <servlet>
                    <servlet-name>JawrCSSServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.properties</param-value>
                    </init-param>
                    <init-param>
                            <param-name>type</param-name>
                            <param-value>css</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
            </servlet>
            
            ...
            <!-- Images mapping -->
            <servlet-mapping>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <url-pattern>*.png</url-pattern>
            </servlet-mapping> 
            <servlet-mapping>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <url-pattern>*.gif</url-pattern>
            </servlet-mapping> 
            <!-- Fonts mapping -->
            <servlet-mapping>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <url-pattern>*.woff</url-pattern>
            </servlet-mapping> 
            <servlet-mapping>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <url-pattern>*.ttf</url-pattern>
            </servlet-mapping> 
            <servlet-mapping>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <url-pattern>*.svg</url-pattern>
            </servlet-mapping> 
            <servlet-mapping>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <url-pattern>*.eot</url-pattern>
            </servlet-mapping> 
            
            <!-- CSS mapping -->
            <servlet-mapping>
                    <servlet-name>JawrCSSServlet</servlet-name>
                    <url-pattern>*.css</url-pattern>
            </servlet-mapping> 
            

Note : The declaration of your binary in the **jawr.binary.resources**
property is not mandatory. This property is used to calculate the
hashcode of the images at the image servlet startup. If a binary
resource is not defined in this property, the hashcode of this resource
will be calculated at runtime. Look for *jawr.binary.resources* at the
[descriptor syntax doc](./descriptor_syntax.html) for more information.


### Examples web.xml configuration

-   Jawr with JS and CSS handling


            <?xml version="1.0" encoding="ISO-8859-1"?> 

            <!DOCTYPE web-app
               PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
               "http://java.sun.com/dtd/web-app_2_3.dtd">
            
            <web-app>

            <display-name>Jawr enabled application</display-name>
            
            <description>
                    
            </description>
            <servlet>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.properties</param-value>
                    </init-param>
                    <load-on-startup>0</load-on-startup>
            </servlet>
            <servlet>
                    <servlet-name>CssServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.properties</param-value>
                    </init-param>
                    <init-param>
                            <param-name>type</param-name>
                            <param-value>css</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
            </servlet>
            
            <servlet-mapping>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <url-pattern>*.js</url-pattern>
            </servlet-mapping>
            <servlet-mapping>
                    <servlet-name>CssServlet</servlet-name>
                    <url-pattern>*.css</url-pattern>
            </servlet-mapping>      
            
    </web-app>


-   Jawr with JS, CSS and image handling


            <?xml version="1.0" encoding="ISO-8859-1"?> 

            <!DOCTYPE web-app
               PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
               "http://java.sun.com/dtd/web-app_2_3.dtd">
            
            <web-app>

            <display-name>Jawr enabled application</display-name>
            
            <description>
                    
            </description>
            <servlet>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.properties</param-value>
                    </init-param>
                    <load-on-startup>0</load-on-startup>
            </servlet>
            <servlet>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>type</param-name>
                            <param-value>binary</param-value>
                    </init-param>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.properties</param-value>
                    </init-param>
                    <!-- The Jawr Image servlet must start before the Jawr CSS Servlet -->
                    <load-on-startup>0</load-on-startup>
            </servlet>
            <servlet>
                    <servlet-name>CssServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.properties</param-value>
                    </init-param>
                    <init-param>
                            <param-name>type</param-name>
                            <param-value>css</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
            </servlet>
            
            <servlet-mapping>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <url-pattern>*.js</url-pattern>
            </servlet-mapping>
            <servlet-mapping>
                    <servlet-name>CssServlet</servlet-name>
                    <url-pattern>*.css</url-pattern>
            </servlet-mapping>      
            <servlet-mapping>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <url-pattern>*.png</url-pattern>
            </servlet-mapping> 
            <servlet-mapping>
                    <servlet-name>JawrBinaryServlet</servlet-name>
                    <url-pattern>*.gif</url-pattern>
            </servlet-mapping> 
            
    </web-app>


### [Using JSON configuration source]()

By default, Jawr use properties file as configuration file. Jawr allows
users to use JSON configuration source by using the
**configPropertiesSourceClass** init param. You can define the location
of the configuration file by using the parameter *configLocation*. This
implementation requires the *Jackson* JSON library. So you'll have to
add it to your classpath. For maven users, you'll have to update your
pom file to add the *jackson* dependencies.

Here is a sample of *jackson* dependency declaration in a pom.xml file :


            ...
            <dependency>
                    <groupId>com.fasterxml.jackson.datatype</groupId>
                    <artifactId>jackson-datatype-json-org</artifactId>
                    <version>2.4.2</version>
            </dependency>
            ...


Here is an example of configuration :


            <servlet>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configPropertiesSourceClass</param-name>
                            <param-value>net.jawr.web.resource.bundle.factory.util.JsonPropertiesSource</param-value>
                    </init-param>
                    <init-param>
                            <param-name>configLocation</param-name>
                            <param-value>/jawr.json</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
            </servlet>
            


### Defining a custom properties source

You can customize the way properties are loaded into Jawr, be it by
reading data from a source other than a .properties file (like, say, a
database), or simply by manually overriding some of the values from the
properties file. To do that you must create an implementation of the
interface
*net.jawr.web.resource.bundle.factory.util.ConfigPropertiesSource* and
then you must declare it when defining the Jawr servlet, by setting the
**configPropertiesSourceClass** init param:


            <servlet>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configPropertiesSourceClass</param-name>
                            <param-value>com.mycompany.mypackage.MyConfigPropertiesSourceImplementation</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
            </servlet>
            


The ConfigPropertiesSource interface declares two methods:


            /**
             * Read/modify configuration from a source (such as a .properties file) and return it. 
             * @return
             */
            public abstract Properties getConfigProperties();
            
            /**
             * Determine if configuration is changed to reconfigure Jawr during development 
             * without having to restart the server. 
             * @return
             */
            public abstract boolean configChanged();


The configChanged method will be called only if you set the
jawr.config.reload.interval parameter in the properties file to any
valid value. This would have Jawr check every n seconds whether
configuration has changed since the last time it was loaded. Therefore,
if configChanged returns true, Jawr will redeploy itself.

If you only intend to override some values of the properties file for
some reason (like mapping the debug mode to some global property from
your own application), the best way to go is to subclass
*net.jawr.web.resource.bundle.factory.util.PropsFilePropertiesSource*.
Check the javadoc for details.


### Defining a custom property resolver

By default no ConfigPropertyResolver is used in Jawr, but you can
customize the way properties are resolved in Jawr.  
The property resolver is used to resolve values from placeholder, which
are like "\${myValueToReplace}". To do that you must create an
implementation of the interface
*net.jawr.web.config.ConfigPropertyResolver* and then you must declare
it when defining the Jawr servlet, by setting the
**configPropertyResolverClass** init param:


            <servlet>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configPropertyResolverClass</param-name>
                            <param-value>com.mycompany.mypackage.MyConfigPropertyResolverImplementation</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
            </servlet>
            


The ConfigPropertyResolver interface declares one method:


            /**
             * Resolve the property. If the property has not been resolved <b>null</b> should be return
             * @param property the property to resolve
             * @return the property value or null if not resolved
             */
            public String resolve(String property);
            


Jawr provides a built-in System property resolver which will find the
value of the properties in the System Property.  
To use this property resolver, you need to configure your servlet as
below :


            <servlet>
                    <servlet-name>JavascriptServlet</servlet-name>
                    <servlet-class>net.jawr.web.servlet.JawrServlet</servlet-class>
                    <init-param>
                            <param-name>configPropertyResolverClass</param-name>
                            <param-value>net.jawr.web.config.ConfigSystemPropertyResolver</param-value>
                    </init-param>
                    <load-on-startup>1</load-on-startup>
            </servlet>
            
