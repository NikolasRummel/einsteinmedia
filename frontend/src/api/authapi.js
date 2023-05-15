import axios from "axios";

let instance = axios.create({
    baseURL: 'http://localhost:8081',
    post: {
        Authorization: sessionStorage.getItem("token")
    }
});

export default instance;
