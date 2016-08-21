Jawr Global Postprocessors
--------------------------

Global Postprocessors are filters that Jawr applies to resources at the
the end of the bundling process. In some cases, it is convenient to
postprocess all the JS or CSS files after making the bundle process,
because you need to gather information from all the bundled files to
make some process on your bundles.

Jawr provides one built-in global postprocessor, which handles the
Google closure compression on all the generated bundles. Jawr uses a
global postprocessor for Google closure compression, because in advanced
mode, the closure compression need to retrieve all the used function to
remove dead code.

You can create and use your own global postprocessor, so here is how to
configure the filter chain for global postprocessing.

A global postprocessor will be only invoked once during application
startup after the bundling and the standard post process phases. The
point is to be able to make a treatment which need to be done on all the
JS or CSS resources, after the bundling process.

For configuration purposes, every global postprocessor has a unique name
key, which you use in a comma-separated property in the descriptor, for
instance:

            jawr.js.bundle.factory.global.postprocessors=closure,myGlobalPostprocessor
                    

In this example, the global postprocessors set for **js** resources are
*closure* and *myGlobalPostprocessor*, meaning that the **smartsprites**
and th **myGlobalPostprocessor** global postprocessors will be processed
for all js resources. The *closure* global postprocessor will be executed before *myGlobalPostprocessor*.


### Custom global postprocessor

You can implement your own global postprocessor components (for js, css,
or both) to perform any functionality not offered by the included one.
To do that, you must create a class with a no-params constructor that
implements the interface
*net.jawr.web.resource.bundle.global.processor.GlobalProcessor&lt;T&gt;*.

This interface is a generic interface where T is the type of context for
the global processing. In our case the type for the global processing
context is
net.jawr.web.resource.bundle.factory.global.postprocessor.GlobalPostProcessingContext,
so we need to implement
*net.jawr.web.resource.bundle.global.processor.GlobalProcessor&lt;GlobalPostprocessingContext&gt;*.

This defines a single method:

            /**
             * Process the bundles for a type of resources.
             *  
             * @param ctx the processing context
             * @param bundles the list of bundles to process.
             */
            public void processBundles(GlobalPostprocessingContext ctx, List bundles);


The first parameter is an object which defines the global postprocessing
context and also gives you acces sto Jawr configuration plus other data
which may be useful under certain circumstances.

The second parameter is the list of bundles defined in your
configuration.

To use this global postprocessor in our application, we need to declare
it in the properties configuration, by giving it a name and declaring
the class so that Jawr may create an instance when starting up. The name
you give to your global postprocessor can then be used to define the
global factory properties, thus allowing you to create a chain that
combines your global postprocessor with those of Jawr.

The name and class are defined by declaring a property in the form
jawr.custom.global.postprocessor.\[name\].class=\[class\]. For example,
the following configuration would add one custom global postprocessor
named *myGlobalpostprocessor* and map to the css resources preprocessing
chains:

    jawr.custom.global.postprocessor.myGlobalPostprocessor.class=net.jawr.test.MyGlobalPostprocessor

    jawr.js.bundle.factory.global.postprocessors=myGlobalPostprocessor


------------------------------------------------------------------------

Grails users will unfortunately need to pack their classes in a jar and
add it to the lib folder of their application. The reason for this is a
known flaw in the Grails classloading strategy that keeps plugins from
accessing application classes.

------------------------------------------------------------------------


### Jawr included global postprocessors reference


[**Google Closure postprocessor**]()
--------------------------------

-   **Properties Key**: closure

The google closure global postprocessor will compress the JS bundles.
You can pass arguments to the closure compiler using the prefix
*jawr.js.closure.*

For instance, you can pass the boolean "third\_party" argument using
*jawr.js.closure.third\_party* in the jawr properties file.

The full list of arguments are define in
*com.google.javascript.jscomp.CommandLineRunner*, so you can check it at
the following [link](http://closure-compiler.googlecode.com/svn/trunk/src/com/google/javascript/jscomp/CommandLineRunner.java)

Please check the
[tutorial](../tutorials/howToUseGoogleClosureCompiler.html) on how to
use the Google Closure postprocessor.

