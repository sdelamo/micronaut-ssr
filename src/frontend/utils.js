
function getSsrUtils() {
    return Java.type("com.poc.micronaut.ssr.Utils");
}

export const fetch = async ({ url }) => {
    const { fetch } = getSsrUtils();
    return await new Promise(fetch(url));
};