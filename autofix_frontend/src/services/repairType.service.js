import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/v1/repair-types');
}

const create = data => {
    return httpClient.post("/api/v1/repair-types", data);
}

const get = id => {
    return httpClient.get(`/api/v1/repair-types/${id}`);
}

const update = (id, data) => {
    return httpClient.put(`/api/v1/repair-types/${id}`, data);
}

const remove = id => {
    return httpClient.delete(`/api/v1/repair-types/${id}`);
}

export default { getAll, create, get, update, remove };