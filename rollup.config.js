import { writeFileSync } from "fs";
import autoprefixer from "autoprefixer";
import postcss from "rollup-plugin-postcss";
import { Minifier } from "css-loader-minify-class";

const isProduction = !process.env.ROLLUP_WATCH;

const minifier = new Minifier({ prefix: "_" }).getLocalIdent;
const minifyClass = (localName) => minifier({ resourcePath: "" }, null, localName);

const generateStylesCljs = (_, json) => {
  const lines = ["(ns app.styles)\n"];
  for (let [key, value] of Object.entries(json)) {
    lines.push(`(def ${key} "${value}")`);
  }
  writeFileSync("src/frontend/styles.cljs", lines.join("\n"));
  console.log("=== Created styles.cljs ===");
};
export default [
  {
    input: "src/frontend/main.scss",
    output: {
      file: "resources/public/assets/css/main.css",
      format: "es"
    },
    plugins: [
      postcss({
        plugins: [autoprefixer],
        extract: true,
        minimize: true,
        sourceMap: false,
        extensions: [".scss"],
        autoModules: false
      })
    ]
  }
];