module.exports = {
	publicPath:	process.env.APP_MODE === 'production' ? '/' : '/',
	outputDir: "../../resources/public",
	devServer: {
		port: 9000,
		proxy: {
			'^/api': {
				target: 'http://localhost:8080',
				ws: true,
				changeOrigin: true
			}
		}
	}
};