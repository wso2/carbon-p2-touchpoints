# Using Carbon P2 Touchpoint
In Carbon 5, there are product-specific runtimes, which the developer will not know about at the time of creating the feature. In order to support dynamically copying files to the runtime location during feature installation, WSO2 has introduced a custom touchpoint.

Shown below is a sample of p2.inf in order to copy a particular file to a runtime location:

> metaRequirements.0.namespace = org.eclipse.equinox.p2.iu <br/>
> metaRequirements.0.name = org.wso2.carbon.p2.touchpoint <br/>
> metaRequirements.0.optional = true
> 
> instructions.configure = \ <br />
> org.wso2.carbon.p2.touchpoint.copy(source:${installFolder}/../lib/features/org.wso2.carbon.touchpoint.sample_${feature.version}/bin/,target:${installFolder}/../\{runtime\}/bin/, overwrite:true);\ <br />
> org.wso2.carbon.p2.touchpoint.copy(source:${installFolder}/../lib/features/org.wso2.carbon.touchpoint.sample_${feature.version}/conf/osgi/launch.properties,target:${installFolder}/../\{runtime\}/conf/osgi/launch.properties, overwrite:true);\

* `{runtime}`: which is replaced with the runtime name at the feature installation

This custom touchpoint should be available in the p2-repo. Therefore it should be added to `generate-repo` goal as below in the product generation:
 
        <plugin>
            <groupId>org.wso2.carbon.maven</groupId>
            <artifactId>carbon-feature-plugin</artifactId>
            <executions>
                <execution>
                    <id>p2-repo-generation</id>
                    <phase>package</phase>
                    <goals>
                        <goal>generate-repo</goal>
                    </goals>
                    <configuration>
                        <targetRepository>file:${basedir}/target/p2-repo</targetRepository>
                        <features>
                            <feature>
                                <id>org.wso2.carbon.p2.touchpoint.feature</id>
                                <version>${carbon.maven.version}</version>
                            </feature>
                        </features>
                    </configuration>
                </execution>
                ...
            </executions>
            ...
        </plugin>


## How To Contribute
* Please report issues at [WSO2 Carbon Issues](https://github.com/wso2/carbon-p2-touchpoints/issues).
* Send your pull requests to [master branch](https://github.com/wso2/carbon-p2-touchpoints/tree/master).
* You can find more instructions on howto contribute on community site (http://wso2.com/community).

## Contact Us
WSO2 developers can be contacted via the mailing lists:
* WSO2 Developers List : dev@wso2.org
* WSO2 Architecture List : architecture@wso2.org
