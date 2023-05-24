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
    return getUser() != null && getAuthKey() != null
}

export function logOut() {
    window.location.reload(false);
    sessionStorage.removeItem('authKey');
    sessionStorage.removeItem('user');
}

export async function fetchUser() {
    if (!isLoggedIn()) return null;
    try {
        const response = await fetch('http://localhost:8081/user', {
            headers: {
                'Authorization': getAuthKey(),
            },
        });

        if (!response.ok) {
            throw new Error('Error fetching user');
        }

        const user = await response.json();
        return user;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export async function fetchUserById(id) {
    try {
        const response = await fetch('http://localhost:8081/user/fetch/' + id, {});
        if (!response.ok) {
            throw new Error('Error retrieving user');
        }
        const json = await response.json();
        return json;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export function fetchFollowers(id) {
    try {
        const response = fetch('http://localhost:8081/followers/count/' + id + "/followers/", {});
        if (!response.ok) {
            throw new Error('Error retrieving followers');
        }
        const json = response.json();
        return json;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export function fetchFollowees(id) {
    try {
        const response = fetch('http://localhost:8081/followers/count/' + id + "/followees/", {});
        if (!response.ok) {
            throw new Error('Error retrieving followers');
        }
        const json = response.json();
        return json;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

