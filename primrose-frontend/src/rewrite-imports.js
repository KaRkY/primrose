const keepSpecifiers = ["withWidth"];

export default function transformer(fileInfo, api, options) {
  const j = api.jscodeshift;
  let hasModifications = false;
  const printOptions = options.printOptions || {
    quote: "single",
    trailingComma: true,
  };

  const importModule = options.importModule || "../components";
  const targetModule = options.targetModule || "../components";

  const root = j(fileInfo.source);
  const importRegExp = new RegExp(`^${importModule}$`);

  root.find(j.ImportDeclaration).forEach(path => {
    const importPath = path.value.source.value;
    let entryModule = importPath.match(importRegExp);

    // Remove non-Material-UI imports
    if (!entryModule) {
      return;
    }

    hasModifications = true;

    path.node.specifiers.forEach(specifier => {
      const localName = specifier.local.name;
      const importedName = specifier.imported ? specifier.imported.name : null;

      let importStatement;
      if(importedName === "withStyles") {
        importStatement = j.importDeclaration(
          [j.importDefaultSpecifier(j.identifier(localName))],
          j.literal(`${targetModule}/styles/${importedName}`),
        );
      } else {
        importStatement = j.importDeclaration(
          [j.importDefaultSpecifier(j.identifier(localName))],
          j.literal(`${targetModule}/${importedName}`),
        );
      }

        j(path).insertBefore(importStatement);
    });

    path.prune();
  });

  return hasModifications ? root.toSource(printOptions) : null;
}