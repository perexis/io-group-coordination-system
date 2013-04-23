LIBDIR=lib
CLOSUREDIR=$(LIBDIR)/closure
SRCDIR=src
BUILDDIR=build
JSDIR=$(SRCDIR)/js
HTMLDIR=$(SRCDIR)/html
CSSDIR=$(SRCDIR)/css
BUILDERPATH=$(CLOSUREDIR)/bin/build/closurebuilder.py
COMPILERPATH=$(LIBDIR)/closure-compiler/compiler.jar
ROOTNAMESPACE="io"

all: lint compile copyres

lint:
	gjslint --nojsdoc --strict -r src/js

fixjs:
	fixjsstyle `find src/js -name '*.js'`

compile:
	mkdir -p $(BUILDDIR)/js
	$(BUILDERPATH) \
		--root=$(CLOSUREDIR) \
		--root=$(JSDIR) \
		--root=$(LIBDIR)/maps \
		--root=$(LIBDIR)/third_party \
		--namespace=$(ROOTNAMESPACE) \
		--output_mode=compiled \
		--compiler_jar=$(COMPILERPATH) \
	  --compiler_flags="--externs=$(LIBDIR)/maps/google_maps_api_v3.js" \
	  --compiler_flags="--compilation_level=ADVANCED_OPTIMIZATIONS" \
	  --compiler_flags="--warning_level=VERBOSE" \
		--compiler_flags="--js=$(CLOSUREDIR)/goog/deps.js"	\
		> $(BUILDDIR)/js/app.js

copyres:
	cp -R $(HTMLDIR)/App.html $(BUILDDIR)
	mkdir -p $(BUILDDIR)/css
	cp -R $(CSSDIR)/*.css $(BUILDDIR)/css
	cp -R $(LIBDIR)/bootstrap/*.css $(BUILDDIR)/css

clean:
		rm -rf `find $(BUILDDIR) | tail -n +2`

.PHONY: clean all lint fixjs

