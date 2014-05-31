# ijignore

.ijignore-controlled selective indexing for IntelliJ® Platform-based products (IDEA, WebStorm, ...).

What it means is that one can control which parts of the projects get indexed and which are not (by means of
.gitignore-like file). More on that below.

> Note that ijignore does not replace [Scopes](http://www.jetbrains.com/idea/webhelp/scopes.html). Nor it obsoletes
"marking directory as Excluded". Think of it as the latter but with a distinction - files are not hidden
from the project's source tree.

## Usage

> At the time of writing ijignore works on [shyiko/intellij-community](https://github.com/shyiko/intellij-community) only.

1. Install [ijignore plugin](https://github.com/shyiko/ijignore/releases).
2. Create `.ijignore` file (ij here stands for IntelliJ®; full name isn't used so that to respect [Trademark Usage Guidelines](http://www.jetbrains.org/display/IJOS/Trademark+Usage+Guidelines)) in the project's root directory.
3. Specify files for (content) indexer to ignore (using the same syntax as in [gitignore](http://git-scm.com/docs/gitignore)).
4. File -> Invalidate Caches / Restart... (obviously you can skip this step if .ijignore was filled in before opening the project).

## Development

```sh
IDEA_CE_HOME=<path to Intellij IDEA> mvn clean package
# check target directory for zip distribution
```

## License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)