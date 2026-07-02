(function () {
    "use strict";

    var storageKey = "tsubuyaki-theme";
    var themes = ["blue", "green", "purple", "red", "orange", "dark"];

    function normalizeTheme(value) {
        return themes.indexOf(value) >= 0 ? value : "blue";
    }

    function applyTheme(theme) {
        var normalizedTheme = normalizeTheme(theme);
        document.documentElement.dataset.theme = normalizedTheme;
        return normalizedTheme;
    }

    function readStoredTheme() {
        try {
            return window.localStorage.getItem(storageKey);
        } catch (error) {
            return null;
        }
    }

    function storeTheme(theme) {
        try {
            window.localStorage.setItem(storageKey, theme);
        } catch (error) {
            return;
        }
    }

    var currentTheme = applyTheme(readStoredTheme());

    window.addEventListener("DOMContentLoaded", function () {
        var selector = document.querySelector(".theme-selector");
        if (!selector) {
            return;
        }

        selector.value = currentTheme;
        selector.addEventListener("change", function (event) {
            currentTheme = applyTheme(event.target.value);
            selector.value = currentTheme;
            storeTheme(currentTheme);
        });
    });
}());
