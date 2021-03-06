Source ordering within bundles
------------------------------

Both Javascript and CSS are interpreted in source order, which means
that often you will need the files within a bundle to follow a certain
sequence. For instance, if your project uses both Prototype.js and
Script.aculo.us you will probably need them to be bundled together, but
you need to make sure that Prototype is always loaded first since
Script.aculo.us depends on it in order to work. If the generated bundle
had Script.aculo.us first, there would be an immediate Javascript error
in any page that loaded it.  

For example, say you have this directory structure containing both
Prototype and Srcipt.aculo.us. You need to load prototype.js first, and
then everything else:

![A sample directory structure](../images/sorting/flat_ptotype.png)
To solve this problem there are three simple ways to force ordering of
resources within a bundle:

-   **Explicitly map files in order**  

   Jawr will add files to bundles in the same order you specify in the
    mappings attribute:


                jawr.js.bundle.foo.id=/bundles/lib.js
                 # Add all files in needed ordering.  
                jawr.js.bundle.foo.mappings=/js/lib/Protoype.js, /js/lib/scriptaculous.js, ...  


   The obvious drawback here is that ordering many files will mean a
    verbose configuration, so this option is recommended only for very
    simple cases.

-   **Implicitly order files by using subfolders and a recurring
    mapping**  

   When doing recursive mapping, Jawr will always include resources
    first, then subdirectories. That means that you can effectively
    force ordering by putting the files which should appear last in
    a subdirectory. For instance, in the previous example, you could
    move all the Script.aculo.us files to a subdir, like this:

   ![A sample directory structure](../images/sorting/sub_ptotype.png)

   Your mapping could then change to this:


                jawr.js.bundle.foo.id=/bundles/lib.js
                jawr.js.bundle.foo.mappings=/js/lib/**  


   Since we have Prototype.js directly under /js/lib and everything
    else in a subfolder, with this mapping we can be sure that our
    bundle will contain the Prototype library first, and then the rest
    of our files.

   **Note :**  
    If you use a mapping like "/js/lib/" or "/js/lib/\*\*", by default
    Jawr will sort the resources in the directory in alphabetical order.

-   **Explicitly order files using a sorting file**  

   Sorting files are simple descriptors that you place at any directory
    where you need to specify ordering. Following the previous examples,
    if we still had Prototype and Script.aculo.us at the same directory
    and we wanted to force ordering, we could create a file named
    **.sorting** at the /js/lib/ directory. The directory structure
    would look like this:

   ![A sample directory structure](../images/sorting/flat_ptotype_license.png)

   The contents of the sorting file would be as follows:

   
                prototype.js

   
   As you can see, only the files that need to go first are added. If
    our mapping was /js/lib/\*\*, we would get prototype.js first, then
    every other resource in the directory in no particular order. If
    there were any subdirectory, its contents would be added afterwards.
     

   Sorting files can also affect subdirs, so you can do things like add
    a resource, then everything in a subdir, then another resource, like
    this:

   
                Prototype.js
                someSubdir/
                effects.js

   
   If our mapping was /js/lib/\*\*, the resulting bundle would contain:

   -   The contents of prototype.js.
   -   The contents of every .js file under /someSubdir/ and
       its subdirectories.
   -   The contents of effects.js.
   -   The contents of every other .js file at the /lib/ folder.
   -   The contents of every file in subdirectories, if any, under
       /lib/ (except /someSubdir/).
