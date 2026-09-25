import api from './api';

export const parseIntent = (text) => api.post('/nlp/parse', { text }).then(res => res.data);