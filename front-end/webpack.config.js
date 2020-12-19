import path from 'path'
import HtmlWebpackPlugin from "html-webpack-plugin";
import webpack from "webpack";

module.exports = {
    entry: 'layout.js',
    module: {
        rules: [
            {test: '/\.css$/', use: ['css-loader', 'style-loader']},
            {test: '/\.js$/', use: 'babel-loader'},

        ]
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: "index_bundle.js"
    },
    plugins: [
        new HtmlWebpackPlugin({template: './src/layout.html'}),
    ]
}
