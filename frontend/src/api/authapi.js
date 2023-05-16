export function storeLoginResponse(json) {
    // Authentifizierungsschlüssel im Session Storage speichern
    sessionStorage.setItem('authKey', json.authKey);
    // Benutzer im Session Storage speichern
    sessionStorage.setItem('user', JSON.stringify(json.user));
}

export function getUser() {
    return JSON.parse(sessionStorage.getItem("user"));
}

export function getAuthKey() {
    return sessionStorage.getItem("authKey")
}

export function isLoggedIn() {
    return getUser() && getAuthKey()
}

export function logOut() {
    window.location.reload(false);
    sessionStorage.removeItem('authKey');
    sessionStorage.removeItem('user');
}

