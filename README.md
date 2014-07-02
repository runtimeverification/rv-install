RV-Install
=========
RV-Install is a unified installer for all RV products.

The idea is to create a single Jar which, when included as a dependency in other projects,
enables the creation of an installer with all necessary and sufficient features for the
support of RV Products.

Requirements
=========
RV-Install requires Java (7+) and ant to build.  Issue the ant command to build, and ant clean
to clean.

The output of ant will be in dist/rv-install-VERSION.jar

The version should be changed in the ant file any time a change is made that could potentially break
installers in a product using RV-Install (RV-Predict, Monitor, and JavaMOP).  The version required is
explicitly defined in the build script of each of the products, which pull their dependencies from
runtimeverification.com/dist/rv-install-VERSION.jar (where it is pushed automatically by a Jenkins
job on master in this repo).

Methodology
=========
RV-Install uses IZPack (currently v4.3) to create Java-based installers that work
cross platform.  Only a single IZPack file is depended on for RV-Install, standalone_compiler.jar
(in the lib/ directory of this project).

Furthermore, the "src" directory defines additional panels and the "res" directory additional
resources included in RV-Install.

To add a custom panel, a Jar must also be created from its sources separately from RV-Install's output
Jar, and added to RV-Install's output Jar (yes, a Jar within a Jar).

This is a requirement of IzPack and helps us avoid compiling from source.  So, to add a custom panel, place
its source in the "src" folder.  When referring to it in XML's, extract the res directory from the RV-Install
Jar and use the RVPanels Jar as the source for your custom panel (see how DependencyPanel is used).
