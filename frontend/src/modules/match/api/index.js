import axios from "axios";

const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 2000
});

export default apiClient;