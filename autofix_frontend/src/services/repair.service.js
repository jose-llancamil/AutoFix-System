import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/v1/repairs');
}

const create = data => {
    return httpClient.post("/api/v1/repairs", data);
}

const get = id => {
    return httpClient.get(`/api/v1/repairs/${id}`);
}

const update = (id, data) => {
    return httpClient.put(`/api/v1/repairs/${id}`, data);
}

const remove = id => {
    return httpClient.delete(`/api/v1/repairs/${id}`);
}

const getTotalRepairCost = id => {
    return httpClient.get(`/api/v1/repairs/${id}/total-cost`);
}

export default { getAll, create, get, update, remove, getTotalRepairCost };
