LIBDIR=lib
CLOSUREDIR=$(LIBDIR)/closure
SRCDIR=src
BUILDDIR=build
JSDIR=$(SRCDIR)/js
HTMLDIR=$(SRCDIR)/html
CSSDIR=$(SRCDIR)/css
BUILDERPATH=$(CLOSUREDIR)/bin/build/closurebuilder.py
COMPILERPATH=$(LIBDIR)/closure-compiler/compiler.jar
ROOTNAMESPACE="io.start"
COMPILATION_LEVEL=ADVANCED_OPTIMIZATIONS

all: lint compile copyres

lint:
	gjslint --nojsdoc --strict `find src/js -maxdepth 0 -name '*.js'`

fixjs:
	fixjsstyle `find src/js -maxdepth 0 -name '*.js'`

gendeps:
	./lib/closure/bin/calcdeps.py -p . -o deps > src/js/gen/deps.js

soy:
	mkdir -p src/js/gen
	java -jar lib/soy/SoyToJsSrcCompiler.jar \
		--shouldProvideRequireSoyNamespaces \
		--shouldGenerateJsdoc \
		--outputPathFormat 'src/js/gen/{INPUT_FILE_NAME_NO_EXT}_soy.js' src/soy/*.soy

quick-compile: COMPILATION_LEVEL=SIMPLE_OPTIMIZATIONS
quick-compile: compile

compile: soy gendeps
	mkdir -p $(BUILDDIR)/js
	$(BUILDERPATH) \
		--root=$(CLOSUREDIR) \
		--root=$(JSDIR) \
		--root=$(LIBDIR)/maps \
		--root=$(LIBDIR)/third_party \
		--root=$(LIBDIR)/soy \
		--namespace=$(ROOTNAMESPACE) \
		--output_mode=compiled \
		--compiler_jar=$(COMPILERPATH) \
	  --compiler_flags="--externs=$(LIBDIR)/maps/google_maps_api_v3.js" \
	  --compiler_flags="--compilation_level=$(COMPILATION_LEVEL)" \
	  --compiler_flags="--warning_level=VERBOSE" \
	  --compiler_flags="--generate_exports" \
		--compiler_flags="--js=$(JSDIR)/gen/deps.js"	\
		> $(BUILDDIR)/js/app.js

copyres:
	cp -R $(HTMLDIR)/App.html $(BUILDDIR)
	mkdir -p $(BUILDDIR)/css
	cp -R $(CSSDIR)/*.css $(BUILDDIR)/css
	cp -R $(LIBDIR)/bootstrap $(BUILDDIR)

clean:
		rm -rf `find $(BUILDDIR) | tail -n +2`

.PHONY: clean all lint fixjs

