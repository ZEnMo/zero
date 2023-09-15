// this is necessary for Jest
module.exports = {
    presets: [
        ['@babel/preset-env', {targets: {node: 'current'}}],
        ['@babel/preset-react', {runtime: 'automatic'}],
        '@babel/preset-typescript',
    ],
    plugins: [
        ["@emotion/babel-plugin"]
    ],
}
