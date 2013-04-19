LIBDIR=lib
CLOSUREDIR=$(LIBDIR)/closure
SRCDIR=src
BUILDDIR=build
JSDIR=$(SRCDIR)/js
HTMLDIR=$(SRCDIR)/html
BUILDERPATH=$(CLOSUREDIR)/bin/build/closurebuilder.py
COMPILERPATH=$(LIBDIR)/closure-compiler/compiler.jar
ROOTNAMESPACE="io.app"

all: compile copyres

compile:
	mkdir -p $(BUILDDIR)/js
	$(BUILDERPATH) --root=$(CLOSUREDIR) --root=$(JSDIR) --namespace=$(ROOTNAMESPACE) \
		--output_mode=compiled --compiler_jar=$(COMPILERPATH) \
	  --compiler_flags="--compilation_level=ADVANCED_OPTIMIZATIONS" \
	  --compiler_flags="--warning_level=VERBOSE" \
		> $(BUILDDIR)/js/app.js

copyres:
	mkdir -p $(BUILDDIR)/html
	cp -R $(HTMLDIR)/*.html $(BUILDDIR)/html

clean:
		rm -rf `find $(BUILDDIR) | tail -n +2`

.PHONY: clean all

