import * as authApi from "./authApi";

export function getUser() {
    return JSON.parse(sessionStorage.getItem("user"));
}

export async function fetchUsers() {
    try {
        const response = await fetch(`http://localhost:8081/user/all`);
        return await response.json();
    } catch (error) {
        console.log('Error by fetching all users', error);
        return -1;
    }
}


export async function fetchUser() {
    if (!authApi.isLoggedIn()) return null;
    try {
        const response = await fetch('http://localhost:8081/user', {
            headers: {
                'Authorization': authApi.getAuthKey(),
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

export async function fetchFollowers(uniqueId) {
    try {
        const response = await fetch('http://localhost:8081/followers/' + uniqueId, {});

        if (!response.ok) {
            throw new Error('Error fetching user');
        }

        return await response.json();

    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export async function fetchFollowees(uniqueId) {
    try {
        const response = await fetch('http://localhost:8081/followers/' + uniqueId + ' /followees', {});

        if (!response.ok) {
            throw new Error('Error fetching user');
        }

        return await response.json();

    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export async function fetchAllFollowments(uniqueId) {
    return await Promise.all([
        fetchFollowers(uniqueId),
        fetchFollowees(uniqueId)
    ])
}

export async function fetchFollowersCount(id) {
    try {
        const response = await fetch(`http://localhost:8081/followers/count/${id}/followers/`);
        const value = await response.json();
        return value;
    } catch (error) {
        console.log('Fehler beim Abrufen der Followee-Anzahl:', error);
        return -1;
    }
}


export async function fetchFolloweesCount(id) {
    try {
        const response = await fetch(`http://localhost:8081/followers/count/${id}/followees/`);
        const value = await response.json();
        return value;
    } catch (error) {
        console.log('Fehler beim Abrufen der Followee-Anzahl:', error);
        return -1;
    }
}

export async function isCurrentUserFollowingUser(uniqueId, uniqueId2) {
    try {
        const response = await fetch(`http://localhost:8081/followers/isFollowing`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                uniqueId: uniqueId,
                uniqueId2: uniqueId2
            })
        });

        if (!response.ok) {
            throw new Error('Fehler beim Überprüfen der Verbindung');
        }

        const isFollowing = await response.json();
        return isFollowing;

    } catch (error) {
        console.error('Fehler:', error);
        throw error;
    }
}

export function sendFollowRequest(email) { //follower from session user
    return fetch("http://localhost:8081/followers/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': authApi.getAuthKey(),
        },
        body: JSON.stringify({
            email: email,
        })
    })
}

export function sendUnFollowRequest(email) { //follower from session user
    return fetch("http://localhost:8081/followers/remove", {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': authApi.getAuthKey(),
        },
        body: JSON.stringify({
            email: email,
        })
    })
}
