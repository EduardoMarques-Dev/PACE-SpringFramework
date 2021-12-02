import baseURL from '../../ambiente/baseURL.js'

export const http = axios.create({
    baseURL: baseURL
});

export default { http };