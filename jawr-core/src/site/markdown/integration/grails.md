Jawr plugin for Grails
----------------------

For a Grails user, it is easy to install Jawr and start using it. The
steps to follow are to install the plugin, configure it with the same
properties format used in the standard version, and add a couple of URL
mapping statements.

------------------------------------------------------------------------

**Warning:** If you are upgrading from a version prior to 2.51, be aware
that Jawr now uses a servlet instead of a controller to serve its files.
This has several advantages: it is faster, there is no need to fiddle
with the grails.mime.file.extensions property, and no controller
mappings need to be added. So if you are upgrading, you need to remove
the controller mappings, reset the grails.mime.file.extensions property
and then follow URL mapping instructions below.

------------------------------------------------------------------------

### Installing the plugin

The Jawr plugin is deployed on the grails central repository.

To use it just add it as a dependency to your BuildConfig.groovy as
followed :

            
            ...
            plugins {
            
            ...
            compile ":jawr:3.8"
            ...
        }


### Configuration

To configure Jawr you must add properties to the Config.groovy file
located at the /conf folder of your application. The syntax you must use
is exactly the same described for the .properties file used in standard
java web applications. Check the [config file syntax
page](../docs/descriptor_syntax.html) for details. Of course, the
property values must be quoted in order to avoid syntax errors. Keep in
mind that there are a few property keys that don't apply when using Jawr
with Grails. Those are:

-   **jawr.config.reload.interval**: The Jawr servlet can be configured
    to listen to changes to the .properties file to reload it when
    it changes. In Grails, Jawr will instead listen to changes to the
    Config.groovy script to reload its configuration. This happens
    automatically when you start Grails in development mode (but keep in
    mind that the changes are applied after you refresh a page in
    your browser).
-   **jawr.charset.name**: The value set for grails.views.gsp.encoding
    will be used for this attribute (which is 'utf-8' by default
    in Grails).
-   **jawr.locale.resolver**: Jawr will use Grails' Locale resolution
    strategy when it applies.

Using this syntax, you will be able to define bundles as specified in
the relevant documentation pages. Note that you are not forced to use
Jawr for both js and css files. If you add none of the jawr.css.\*
parameters, for instance, Jawr will do no effort to serve CSS files.

Jawr uses sl4j to log messages, so you can configure its tracing level
along with the rest of your application. All jawr packages start with
net.jawr.\*, so you can use that as a key for an appender.

The configuration will indicate to the plugin which Jawr servlet to
start.

So if you configure only JS bundle, the plugin will start only the Jawr
JS servlet. So to enable the JS or the CSS servlet, you must at least
define a bundle of the specified type. To enable the Jawr Binary
servlet, which handle images and fonts, you need to define one of the
following property : \* jawr.img.mapping \*
jawr.css.classpath.handle.image \* jawr.binary.\* (Any property which
starts with jawr.binary)


#### URL mapping

There are two ways to map Jawr to requests:

-   You can have Jawr respond to all requests ending in .js and .css,
    thus letting Jawr control all requests to your resources. This is
    the default behavior, and you don't need to configure anything at
    all for it to work, simply omit the jawr.js.mapping/jawr.css.mapping
    properties from your config and you are ready to go.
    -   This is the usual approach, but you may want to bundle only part
        of your code and serve the rest normally. This is specially
        useful if you are adding Jawr to an existing application and you
        do not want to change every existing script tag. Note that Jawr
        handles individual files (you don't need to explicitly define a
        bundle for every file you want to compress and serve
        standalone), so that should not keep you from using this
        mapping method.
-   You can define a URL fragment as a prefix (such as '/jawr/') to
    prefix every URL. Jawr will only serve requests containing
    this prefix. To use this you must add two special parameters to your
    Config.groovy file, to specify the prefixes for js and css:

        jawr.js.mapping = '/script/' // The value can be anything you want for the prefix
        jawr.css.mapping = '/style/'


   Note that the value for these properties **must** be a url-mapping
    string without the wildcard (i.e. with no asterisk).

   With either method, once you have filled out the properties, you
    will be ready to start adding Jawr tags to your pages.

-   Image URL mapping

   By default, Jawr will handle the following image types : .png, .gif,
    .jpg, .jpeg, .ico, .ttf, .eot, .woff

   You can configure the image mapping by settings the
    **jawr.binary.mappings**. If you want to define a set of extension
    file to handle, you need to list the extension with a semi colon
    separator, like this :


        jawr.binary.mapping='*.png; *.jpg; *.gif'


### Using Plugin resource

Jawr Grails Plugin support the use of grails plugins web resources.  
The reference of the web resource must start with **'/plugins/'**
followed by the name of the plugin (with or without its version number)
and finally the location of the resources in the webapp folder of the plugin.

We will take the example of the jquery plugin

![JQuery plugin structure](../images/grails/pluginStructure.png)
Here is an example of configuration where we reference the jquery script :


    jawr.js.bundle.common.id='/js/common.js'
    jawr.js.bundle.common.mappings='/plugins/jquery/js/jquery/jquery-1.11.1.js'


You can also reference it with the version number but this is not
recommended as you will need to change your configuration for each
version modification.


    jawr.js.bundle.common.id='/js/common.js'
    jawr.js.bundle.common.mappings='/plugins/jquery-1.11.1/js/jquery/jquery-1.11.1.js'

### i18n message

Grails support in standard the internationalisation of message in the
application using the grails-app/i18n/messages resource bundle.  
Please check the [message generator documentation](../docs/messages_gen.html) for more information on the usage of grails i18n messages in Jawr.


### Using the tag library

The tag library for grails works exactly the same as the JSP version, so
you will find all the details in the [JSP taglib documentation page](../docs/taglibs.html).

The only difference is that in GSP pages there is no need to import the
tags at all. Also, keep in mind that the namespace for the Jawr taglib
is **jawr:**.

This is how you would use the tags in a typical page:


    <html>
     <head>
            <jawr:script src="/bundles/yui.js"  />
            <jawr:style src="/bundles/all.css" />
     </head>
     <body>
            ...
            <jawr:img src="/images/logo.png" />
     </body>
    </html>


### Sample application

You will find below the link to a sample application source code showing
the integration of Jawr and Grails :

[source code](https://github.com/ic3fox/jawr-grails/tree/master/jawr-grails-sample)

