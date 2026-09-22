import { useEffect, useState } from 'react';
import { getTasks, createTask, deleteTask, updateTask } from '../services/taskService';
import { createReminder } from '../services/reminderService';
import { logout } from '../services/authService';

export default function DashboardScreen() {
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState('');
  const [remindingTaskId, setRemindingTaskId] = useState(null);
  const [remindAt, setRemindAt] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    getTasks()
      .then(setTasks)
      .catch(() => setError('Failed to load tasks. Is the backend running?'))
      .finally(() => setLoading(false));
  }, []);

  const handleAdd = async () => {
    if (!title.trim()) return;
    const newTask = await createTask({ title });
    setTasks([...tasks, newTask]);
    setTitle('');
  };

  const handleToggle = async (task) => {
    const updated = await updateTask(task.id, { ...task, completed: !task.completed });
    setTasks(tasks.map(t => t.id === task.id ? updated : t));
  };

  const handleDelete = async (id) => {
    await deleteTask(id);
    setTasks(tasks.filter(t => t.id !== id));
  };

  const handleSetReminder = async (taskId) => {
    if (!remindAt) return;
    await createReminder(taskId, remindAt);
    setRemindingTaskId(null);
    setRemindAt('');
    alert('Reminder set!');
  };

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Your Tasks</h1>
        <button className="logout-btn" onClick={logout}>Logout</button>
      </div>

      {loading && <p className="status-msg">Loading tasks...</p>}
      {error && <p className="status-msg error">{error}</p>}

      <div className="task-input-row">
        <input
          className="task-input"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="New task"
          onKeyDown={(e) => e.key === 'Enter' && handleAdd()}
        />
        <button className="add-btn" onClick={handleAdd}>Add</button>
      </div>

      {tasks.length === 0 ? (
        <p className="empty-state">No tasks yet — add one above</p>
      ) : (
        <ul className="task-list">
          {tasks.map((task) => (
            <li key={task.id} className="task-item">
              <div className="task-row">
                <input type="checkbox" checked={task.completed} onChange={() => handleToggle(task)} />
                <span className={task.completed ? 'task-done' : ''}>{task.title}</span>
                <button className="reminder-btn" onClick={() => setRemindingTaskId(task.id)}>⏰</button>
                <button className="delete-btn" onClick={() => handleDelete(task.id)}>✕</button>
              </div>

              {remindingTaskId === task.id && (
                <div className="reminder-form">
                  <input
                    type="datetime-local"
                    value={remindAt}
                    onChange={(e) => setRemindAt(e.target.value)}
                  />
                  <button onClick={() => handleSetReminder(task.id)}>Set</button>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}