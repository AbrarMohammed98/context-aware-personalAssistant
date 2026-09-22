import api from './api';

export const getRemindersForTask = (taskId) =>
  api.get(`/reminders/task/${taskId}`).then(res => res.data);

export const createReminder = (taskId, remindAt) =>
  api.post('/reminders', { task: { id: taskId }, remindAt }).then(res => res.data);