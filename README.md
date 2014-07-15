# Abide : Rules

The [**abide** verification framework](https://github.com/samarion/scala-abide) is built in an extensible way that enables
simple user-defined rule addition. To provide a high(er) global rule quality and ease of access, rules are grouped in this
repository and should be submitted as pull requests for incorporation.

## Contributing a rule

For now, **abide** only supports traversal rules but further analysis techniques should appear in future releases.

Once a new rule has been defined, it must be added to a rule project. These are defined in the `project/Build.scala` sbt build configuration as sub-projects which are then added to `lazy val rules : Seq[Project]`. All such rule packages will be separately exported as jars that can be selectively used for project verification. Typically, unless the new rule must
logically belong to a new package, it suffices to place the rule inside one of the existing rule `src` folders and it will
automatically be build alongside the pre-existing rules.

To specify all rules provided by a rule package, each project must contain an xml file `resources/abide-plugin.xml` that specifies which rule classes are provided by that particular package. Build tool integration relies on these xml files to reflectively instantiate rules, so extending these files is mandatory for each new rule. The file structure is as follows:
```xml
<plugin>
  <rule class="com.example.rule.MyRule1" />
  <rule class="com.example.rule.MyRule2" />
  ...
</plugin>
```

Finally, to make sure new extensions are clear/valid and remain so, each rule must feature a short descriptive entry in the wiki that clarifies use cases and analysis outputs as well as some unit tests that will help avoid breakage.
