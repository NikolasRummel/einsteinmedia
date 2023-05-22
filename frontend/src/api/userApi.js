import * as authApi from "./authApi";

export function getUser() {
    return JSON.parse(sessionStorage.getItem("user"));
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

export function isCurrentUserFollowingUser(userId) {
    //return if currend user follows an other
    return false
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

