/**
 * Provides Cross-Origin Resource Sharing (CORS) support for Amaya framework applications.
 * <p>
 * This module includes:
 * <ul>
 *     <li>Builders and configurers for CORS policies ({@link io.github.amayaframework.cors.CorsConfigBuilder}, {@link io.github.amayaframework.cors.CorsApplicationConfigurer})</li>
 *     <li>Default CORS headers and constants ({@link io.github.amayaframework.cors.CorsHeaders}, {@link io.github.amayaframework.cors.ProxyHeaders}, {@link io.github.amayaframework.cors.CorsDefaults})</li>
 *     <li>Task for handling CORS in HTTP requests ({@link io.github.amayaframework.cors.CorsTask})</li>
 * </ul>
 * <p>
 * Dependencies include core Amaya modules for HTTP handling, web applications, options management,
 * and tokenization, as well as utility libraries for synchronous and type-safe operations.
 */
module amayaframework.cors {
    // Imports
    // Basic dependencies
    requires amayaframework.tokenize;
    requires com.github.romanqed.jtype;
    // Amaya modules
    requires amayaframework.options;
    requires amayaframework.web;
    // Exports
    exports io.github.amayaframework.cors;
}
