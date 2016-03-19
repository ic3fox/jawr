JMX support in JAWR
-------------------

The Java Management Extensions (JMX) API is standard API introduced in
J2SE 5.0 for managing and monitoring applications and services.

We have added the support to JMX in Jawr to allow user to modify the
behaviour of Jawr at runtime.

For example, the user will be able to pass an entire application from
production mode to debug mode and vice versa. The user will also be able
to set one session in debug mode, while the rest of the application will
still be in production mode.

This is a really powerful way to interact with Jawr at runtime. To
access to the Jawr MBeans, you will have to use a JMX console, like
JConsole, which is provided in the JDK since Java 5.0.\
The Jawr MBeans are available accessible with the type
**net.jawr.web.jmx**.

When using Jawr in multiple applications on your application server, it
can be useful to set the prefix of the Jawr MBean for applications which
are deployed on different server using the same application context.
This will allows you to know which MBean is associated to which
application. To do that, you have to use the **jawr.jmx.mbean.prefix**
property in your Jawr configuration file.

**Important note :** This feature will allow you to modify some Jawr
properties at runtime, but you should keep in mind that your changes are
not persisted. This means that if you made a modification at runtime,
the next time you will restart your application, your latest
modifications made at runtime, will not be taken into account.

![](../images/jmx/jawr_JConsole.png)
You will find an overview of JMX at the following links :

-   <http://java.sun.com/j2se/1.5.0/docs/guide/jmx/overview/JMXoverviewTOC.html>
-   <http://java.sun.com/j2se/1.5.0/docs/guide/management/agent.html>


### JMX Setup

The JMX is part of Java API since Java 5.

To enable it, you only have to pass the following argument in the
virtual machine of your web application server:


            -Dcom.sun.management.jmxremote


### Secure access to JMX MBeans

To secure the JMX access to your application, please look for the
section "Using Password and Access Files" at the following link.

-   <http://java.sun.com/j2se/1.5.0/docs/guide/management/agent.html>


### JAWR MBeans

We have defined two types of MBeans in JAWR. One which handles the
configuration for a resource type (JS, CSS, Image). And another one,
which handles the configuration for an entire application.

-   JawrConfigManagerMBean

This MBean will handle the configuration for one type of resource.
You will be able to configure the following properties :

   -   **charsetName** the charset name
   -   **cssLinkFlavor** the CSS link flavor
   -   **debugModeOn** the flag indicating if we are in debug mode or
       not
   -   **debugOverrideKey** the debug override key
   -   **gzipResourcesForIESixOn** the flag indicating if we must GZIP
       resources for IE 6
   -   **gzipResourcesModeOn** the flag indicating if we must GZIP
       resources
   -   **contextPathOverride** the value which should be used to
       override the context path
   -   **contextPathSslOverride** the value which should be used to
       override the context path for SSL request
   -   **useContextPathOverrideInDebugMode** the flag indicating if we
       should the the overridden value of the context path in debug
       mode
   -   **dwrMapping** the dwr mapping
   -   **imageBundleDefinition** the image bundle definition
   -   **imageHashAlgorithm** the image hash algorithm
   -   **usingClasspathCssImageServlet** the flag indicating if we
       should use Image Servlet for CSS images of classpath
       CSS resources.
   -   **useBundleMapping** the flag indicating if we should use the
       bundle mapping
   -   **jawrWorkingDirectory** the JAWR working directory.

You could modify these values at runtime, but the values will only
   be taken in account when you will launch the **refreshConfig**
    operation from the JMX console :

                /**
                 * Refresh the configuration. 
                 */
                public void refreshConfig();


-   JawrApplicationConfigManagerMBean

This MBean will handle the configuration for an entire application.
    This means that when modifying the configuration on this MBean, you
    will modify the configuration of JAWR for all resources (JS, CSS
    and Images).

You will be able to configure the following properties :

   -   **charsetName** the charset name
   -   **cssLinkFlavor** the CSS link flavor
   -   **debugModeOn** the flag indicating if we are in debug mode or
       not
   -   **debugOverrideKey** the debug override key
   -   **gzipResourcesForIESixOn** the flag indicating if we must GZIP
       resources for IE 6
   -   **gzipResourcesModeOn** the flag indicating if we must GZIP
       resources
   -   **contextPathOverride** the value which should be used to
       override the context path
   -   **contextPathSslOverride** the value which should be used to
       override the context path for SSL request
   -   **useContextPathOverrideInDebugMode** the flag indicating if we
       should the the overridden value of the context path in debug
       mode
   -   **useBundleMapping** the flag indicating if we should use the
       bundle mapping
   -   **jawrWorkingDirectory** the JAWR working directory.
 
 You could modify these values at runtime, but the values will only
    be taken in account when you will launch the **refreshConfig**
    operation from the JMX console :

                 /**
                 * Refresh the configuration. 
                 */
                public void refreshConfig();

 
There are other methods available in the JawrApplicationConfigManagerMBean.

                /**
                 * Add a session ID to the set of debug session ID.
                 * All request make by sessions, whose their IDs are contained in the debug session set,
                 * will be threated as in debug mode.
                 *   
                 * @param sessionId the session ID to add
                 */
                public void addDebugSessionId(String sessionId);
                
                /**
                 * Add a session ID, to the set of debug session ID.
                 * All request make by sessions, whose their IDs are contained in the debug session set,
                 * will be threated as in debug mode.
                 *   
                 * @param sessionId the session ID to add
                 */
                public void removeDebugSessionId(String sessionId);

                /**
                 * Remove a session ID from the set of debug session ID.
                 *   
                 * @param sessionId the session ID to remove
                 */
                public void removeAllDebugSessionId();
                
                /**
                 * Returns true if the session ID passed in parameter is a debuggable session ID
                 * @param sessionId the session ID
                 * @return true if the session ID passed in parameter is a debuggable session ID
                 */
                public boolean isDebugSessionId(String sessionId);
                
