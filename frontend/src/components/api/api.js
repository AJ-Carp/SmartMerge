import axios from "axios";
const backendBaseUrl = import.meta.env.VITE_API_URL;

const api = axios.create({
    baseURL: `${backendBaseUrl}/api/v1`,
    withCredentials: true,
})

api.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.data?.error === "Invalid JWT") {
            localStorage.removeItem('userProfile');
            window.location.href = "/";
        }
        return Promise.reject(error);
    }
);

export default api;