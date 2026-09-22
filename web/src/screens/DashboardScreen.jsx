import { useEffect, useState } from 'react';
import { getTasks, createTask, deleteTask, updateTask } from '../services/taskService';
import { logout } from '../services/authService';

export default function DashboardScreen() {
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState('');

  useEffect(() => {
    getTasks().then(setTasks);
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

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Your Tasks</h1>
        <button className="logout-btn" onClick={logout}>Logout</button>
      </div>

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
                <input
                  type="checkbox"
                  checked={task.completed}
                  onChange={() => handleToggle(task)}
                />
                <span className={task.completed ? 'task-done' : ''}>{task.title}</span>
                <button className="delete-btn" onClick={() => handleDelete(task.id)}>✕</button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}