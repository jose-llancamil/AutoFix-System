import httpClient from "../http-common";

const getAllBonuses = () => {
    return httpClient.get('/api/v1/bonuses');
}

const getBonus = id => {
    return httpClient.get(`/api/v1/bonuses/${id}`);
}

const createBonus = data => {
    return httpClient.post("/api/v1/bonuses", data);
}

const updateBonus = (id, data) => {
    return httpClient.put(`/api/v1/bonuses/${id}`, data);
}

const deleteBonus = id => {
    return httpClient.delete(`/api/v1/bonuses/${id}`);
}

const applyBonusToVehicle = (vehicleId, bonusId) => {
    return httpClient.post(`/api/v1/bonuses/apply/${vehicleId}/${bonusId}`);
}

export default { getAllBonuses, getBonus, createBonus, updateBonus, deleteBonus, applyBonusToVehicle };