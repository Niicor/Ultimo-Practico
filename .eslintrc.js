// .eslintrc.js
module.exports = {
  env: {
    node: true, // Si usas Node.js
    browser: true, // Si usas un navegador
  },
  extends: [
    "eslint:recommended", // Reglas recomendadas de ESLint
    "plugin:vue/vue3-recommended", // Reglas recomendadas para Vue 3
    "plugin:prettier/recommended", // Integra Prettier con ESLint
  ],
  rules: {
    // Aquí puedes añadir o sobrescribir reglas de ESLint
  },
  parserOptions: {
    ecmaVersion: "latest", // Especifica la versión de ECMAScript
    sourceType: "module", // Si usas módulos ES
  },
};
