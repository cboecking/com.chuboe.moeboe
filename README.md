<h1><img src="http://enroute.osgi.org/img/enroute-logo-64.png" witdh=40px style="float:left;margin: 0 1em 1em 0;width:40px">
OSGi enRoute Archetype</h1>

This repository represents a template workspace for bndtools, it is the easiest way to get started with OSGi enRoute. The workspace is useful in an IDE (bndtools or Intellij) and has support for [continuous integration][2] with [gradle][3]. If you want to get started with enRoute, then follow the steps in the [quick-start guide][1].

[1]: http://enroute.osgi.org/quick-start.html
[2]: http://enroute.osgi.org/tutorial_base/800-ci.html
[3]: https://www.gradle.org/

To add to your repository, copy and paste the following into a bundle descriptor in your cnf=>ext directory:
-plugin.moeboe.po = \
  aQute.bnd.deployer.repository.FixedIndexedRepo; \
    name        =       moeboe-po; \
    locations   =       https://raw.githubusercontent.com/cboecking/com.chuboe.moeboe/master/cnf/release/index.xml
