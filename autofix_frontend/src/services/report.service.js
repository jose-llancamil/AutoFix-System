import httpClient from "../http-common";

const generateRepairCostReport = () => {
    return httpClient.get('/api/v1/reports/repair-costs');
}

const generateRepairTypeSummaryReport = () => {
    return httpClient.get('/api/v1/reports/repair-type-summary');
}

const generateAverageRepairTimesReport = () => {
    return httpClient.get('/api/v1/reports/average-repair-times');
}

const generateRepairTypesEngineSummary = () => {
    return httpClient.get('/api/v1/reports/repair-types-engine-summary');
}

export default {
    generateRepairCostReport,
    generateRepairTypeSummaryReport,
    generateAverageRepairTimesReport,
    generateRepairTypesEngineSummary
};
