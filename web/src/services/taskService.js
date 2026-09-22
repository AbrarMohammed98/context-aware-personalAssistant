import api from './api';

export const getTasks = () => api.get('/tasks').then(res => res.data);
export const createTask = (task) => api.post('/tasks', task).then(res => res.data);
export const deleteTask = (id) => api.delete(`/tasks/${id}`);
export const updateTask = (id, updates) => api.put(`/tasks/${id}`, updates).then(res => res.data);