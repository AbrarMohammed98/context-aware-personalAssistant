import { useEffect, useState } from 'react';
import { getTasks, createTask, deleteTask, updateTask } from '../services/taskService';
import { createReminder } from '../services/reminderService';
import { logout } from '../services/authService';
import toast from 'react-hot-toast';

function formatDateTime(isoString) {
  const date = new Date(isoString);
  return date.toLocaleString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
  });
}

export default function DashboardScreen() {
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState('');
  const [remindingTaskId, setRemindingTaskId] = useState(null);
  const [remindAt, setRemindAt] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [dueDate, setDueDate] = useState('');
  const [taskReminders, setTaskReminders] = useState({});

  useEffect(() => {
    getTasks()
      .then(setTasks)
      .catch(() => setError('Failed to load tasks. Is the backend running?'))
      .finally(() => setLoading(false));
  }, []);

  const handleAdd = async () => {
    if (!title.trim()) return;
    try {
      const newTask = await createTask({ title, dueAt: dueDate || null });
      setTasks(prev => [...prev, newTask]);
      setTitle('');
      setDueDate('');
      toast.success('Task added');
    } catch {
      toast.error('Could not add task');
    } 
  };

  const handleToggle = async (task) => {
    try {
      const updated = await updateTask(task.id, { ...task, completed: !task.completed });
      setTasks(prevTasks => prevTasks.map(t => t.id === task.id ? updated : t));
    } catch {
      toast.error('Could not update task');
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteTask(id);
      setTasks(prevTasks => prevTasks.filter(t => t.id !== id));
      toast.success('Task deleted');
    } catch {
      toast.error('Could not delete task');
    }
  };

  const handleSetReminder = async (taskId) => {
    if (!remindAt) return;
    try {
      await createReminder(taskId, remindAt);
      setTaskReminders(prev => ({ ...prev, [taskId]: remindAt }));
      setRemindingTaskId(null);
      setRemindAt('');
      toast.success('Reminder set!');
    } catch {
      toast.error('Could not set reminder');
    }
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
        <input
          type="datetime-local"
          className="task-input"
          value={dueDate}
          onChange={(e) => setDueDate(e.target.value)}
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
              {task.dueAt && (
                <p className="task-due">Due: {formatDateTime(task.dueAt)}</p>
              )}
              {taskReminders[task.id] && (
                <p className="task-reminder">🔔 Reminder: {formatDateTime(taskReminders[task.id])}</p>
              )}

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