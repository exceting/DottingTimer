define(function(require) {
    require.async(['global/Variable', 'global/Context'], function() {
        Backbone.history.start({
            pushState: false,
            root: location.pathname
        });
    });
});