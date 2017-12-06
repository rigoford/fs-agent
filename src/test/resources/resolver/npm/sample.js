/*global module:false,__dirname:true*/
module.exports = function(grunt) {
    //--------------------------------------------------
    //------------Project config------------------------
    //--------------------------------------------------
    grunt.initConfig({
        // Metadata.
        pkg: grunt.file.readJSON('package.json'),
        banner: '/*! <%= pkg.title || pkg.name %> - v<%= pkg.version %>\n' +
        '<%= pkg.homepage ? "* " + pkg.homepage + "\\n" : "" %>' +
        '* Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author.name %>;' +
        ' Released under the <%= pkg.license %> License */\n',
        // Task configuration.
        shell: {
            oldTests: {
                command: 'node test/server.js',
                options: {
                    stdout: true,
                    failOnError: true
                }
            },
            testRhino : {
                command : function() {
                    var commandList = [],
                        fs = require('fs'),
                        rhinoFolder = 'test/rhino/';
                    fs.readdirSync(__dirname  + '/' + rhinoFolder + '/lib').forEach( function(rhinoJar) {
                        if(rhinoJar.indexOf('.jar') >= 0) {
                            commandList.push('java -jar ' + rhinoFolder + '/lib/' + rhinoJar + ' -f ' + rhinoFolder + '/rhinoTest.js');
                        }
                    });
                    return commandList.join(' && ');
                },
                options : {
                    stdout: true,
                    failOnError: true
                }
            },
            // these are old that should eventually be rewritten to take advantage of grunt and the current build process
            buildParser: {
                command: 'node src/build.js',
                options: {
                    stdout: true
                }
            },
            bench: {
                command: 'node benchmark/server.js',
                options: {
                    stdout: true
                }
            },
            doc: {
                command: 'node docs/build.js',
                options: {
                    stdout: true
                }
            },
            gitAddArchive: {
                command: 'git add <%= compress.distTarBall.options.archive %> <%= compress.distZip.options.archive %>',
                options: {
                    stdout: true
                }
            }
        },
        concat: {
            options: {
                banner: '<%= banner %>',
                stripBanners: true
            },
            core: {
                src: ['lib/dust.js'],
                dest: 'tmp/dust-core.js'
            },
            full: {
                src: ['lib/dust.js', 'lib/parser.js', 'lib/compiler.js'],
                dest: 'tmp/dust-full.js'
            }
        },
        copy: {
            core: {
                src: 'tmp/dust-core.js',
                dest: 'dist/dust-core.js'
            },
            coreMin: {
                src: 'tmp/dust-core.min.js',
                dest: 'dist/dust-core.min.js'
            },
            full: {
                src: 'tmp/dust-full.js',
                dest: 'dist/dust-full.js'
            },
            fullMin: {
                src: 'tmp/dust-full.min.js',
                dest: 'dist/dust-full.min.js'
            },
            license: {
                src: 'LICENSE',
                dest: 'dist/'
            }
        },
        uglify: {
            options: {
                banner: '<%= banner %>',
                mangle: {
                    except: ['require', 'define', 'module', 'dust']
                }
            },
            core: {
                src: '<%= concat.core.dest %>',
                dest: 'tmp/dust-core.min.js'
            },
            full: {
                src: '<%= concat.full.dest %>',
                dest: 'tmp/dust-full.min.js'
            }
        },
        compress: {
            distTarBall: {
                options: {
                    archive: 'archive/dust-<%= pkg.version %>.tar.gz',
                    mode: 'tgz',
                    pretty: true
                },
                files: [
                    {expand: true, cwd: 'dist', src: ['**'], dest: 'dust-<%= pkg.version %>/', filter: 'isFile'}
                ]
            },
            distZip: {
                options: {
                    archive: 'archive/dust-<%= pkg.version %>.zip',
                    mode: 'zip',
                    pretty: true
                },
                files: [
                    {expand: true, cwd: 'dist', src: ['**'], dest: 'dust-<%= pkg.version %>/', filter: 'isFile'}
                ]
            }
        },
        clean: {
            build: ['tmp/*'],
            dist: ['dist/*']
        },
        jshint: {
            options: {
                jshintrc: true
            },
            gruntfile: {
                src: 'Gruntfile.js'
            },
            libs: {
                src: ['lib/**/*.js', 'src/**/*.js', '!lib/parser.js'] // don't hint the parser which is autogenerated from pegjs
            }
        },
        connect: {
            testServer: {
                options: {
                    port: 3000,
                    keepalive: false
                }
            }
        },
        jasmine: {
            /*tests production (minified) code*/
            testProd: {
                src: 'tmp/dust-full.min.js',
                options: {
                    keepRunner: false,
                    specs: ['test/jasmine-test/spec/**/*.js']
                }
            },
            /*tests unminified code, mostly used for debugging by `grunt dev` task*/
            testDev : {
                src: 'tmp/dust-full.js',
                options: {
                    keepRunner: false,
                    specs : '<%=jasmine.testProd.options.specs%>'
                }
            },
            /*runs unit tests with jasmine and collects test coverage info via istanbul template
            * tests unminified version of dust to make sure test coverage starts are correctly calculated
            * istanbul jumbles source code in order to record coverage, so this task is not suited for
            * debugging while developing. Use jasmine:testClient instead, which runs on unminified code
            * and can be easily debugged*/
            coverage : {
                src: 'tmp/dust-full.js',
                options: {
                    keepRunner: false,
                    specs : '<%=jasmine.testProd.options.specs%>',
                    template: require('grunt-template-jasmine-istanbul'),
                    templateOptions: {
                        coverage: 'tmp/coverage/coverage.json',
                        report: 'tmp/coverage',
                        thresholds: {
                            lines: 82,
                            statements: 82,
                            branches: 70,
                            functions: 88
                        }
                    }
                }
            }
        },
        watch: {
            lib: {
                files: ['<%= jshint.libs.src %>'],
                tasks: ['clean:build', 'buildLib']
            },
            gruntfile: {
                files: '<%= jshint.gruntfile.src %>',
                tasks: ['jshint:gruntfile']
            },
            lib_test: {
                files: ['<%= jshint.libs.src %>', '<%= jasmine.testProd.options.specs %>'],
                tasks: ['testPhantom']
            }
        },
        bump: {
            options: {
                files: ['package.json', 'bower.json'],
                updateConfigs: ['pkg'],
                push: false,
                commitFiles: ['-a']
            }
        },
        log: {
            coverage: {
                options: {
                    message: 'Open <%=jasmine.coverage.options.templateOptions.report%>/index.html in a browser to view the coverage.'
                }
            },
            copyForRelease: {
                options: {
                    message: 'OK. Done copying version <%= pkg.version %> build from tmp to dist'
                }
            },
            testClient: {
                options: {
                    message: 'Open http://localhost:<%= connect.testServer.options.port %>/_SpecRunner.html in a browser\nCtrl + C to stop the server.'
                }
            },
            release: {
                options: {
                    message: ['OK. Done bumping, adding, committing, tagging and pushing the new version',
                        '',
                        'You still need to manually do the following:',
                        '  * git push upstream && git push upstream --tags',
                        '  * npm publish'].join('\n')
                }
            }
        }
    });

    //--------------------------------------------------
    //------------Custom tasks -------------------------
    //--------------------------------------------------
    grunt.registerMultiTask('log', 'Print some messages', function() {
        grunt.log.ok(this.data.options.message);
    });

    //--------------------------------------------------
    //------------External tasks -----------------------
    //--------------------------------------------------
    // These plugins provide necessary tasks.
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jasmine');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-compress');
    grunt.loadNpmTasks('grunt-gh-pages');
    grunt.loadNpmTasks('grunt-bump');
    grunt.loadNpmTasks('grunt-shell');

    //--------------------------------------------------
    //------------Grunt task aliases -------------------
    //--------------------------------------------------
    grunt.registerTask('buildLib',       ['jshint:libs', 'concat']);
    grunt.registerTask('build',          ['clean:build', 'shell:buildParser', 'buildLib', 'uglify']);

    //test tasks
    grunt.registerTask('testNode',       ['shell:oldTests']);
    grunt.registerTask('testRhino',      ['build', 'shell:testRhino']);
    grunt.registerTask('testPhantom',    ['build', 'jasmine:testProd']);
    grunt.registerTask('test',           ['build', 'jasmine:testProd', 'shell:oldTests', 'shell:testRhino', 'jasmine:coverage']);

    //task for debugging in browser
    grunt.registerTask('dev',            ['build', 'jasmine:testDev:build', 'connect:testServer','log:testClient', 'watch:lib']);

    //task to run unit tests on client against prod version of code
    grunt.registerTask('testClient',     ['build', 'jasmine:testProd:build', 'connect:testServer', 'log:testClient', 'watch:lib_test']);

    //coverage report
    grunt.registerTask('coverage',       ['build', 'jasmine:coverage', 'log:coverage']);

    //release tasks
    grunt.registerTask('copyForRelease', ['clean:dist', 'copy:core', 'copy:coreMin', 'copy:full', 'copy:fullMin', 'copy:license', 'log:copyForRelease']);
    grunt.registerTask('buildRelease',   ['test', 'copyForRelease', 'compress']);
    grunt.registerTask('releasePatch',   ['bump-only:patch', 'buildRelease', 'shell:gitAddArchive', 'bump-commit', 'log:release']);
    grunt.registerTask('releaseMinor',   ['bump-only:minor', 'buildRelease', 'shell:gitAddArchive', 'bump-commit', 'log:release']);
    // major release should probably be done with care
    //grunt.registerTask('releaseMajor',   ['bump-only:major', 'buildRelease', 'bump-commit:major', 'log:release']);

    //default task - full test
    grunt.registerTask('default',        ['test']);
};
