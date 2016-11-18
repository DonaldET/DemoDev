Sample parameters for run-time testing in Eclipse launch

-defaultContext classpath:model_read_tests/MyTestAppModel.properties
-overrideContextList "classpath:model_read_tests/MyTestAppOverride.properties,classpath:model_read_tests/MyTestAppOverride2.properties"
-dstDir ./
-generatedFileList MyGenWrapper.txt
-srcDir src/test/resources/tpl_read_tests
-templateList MyGenWrapper.tpl