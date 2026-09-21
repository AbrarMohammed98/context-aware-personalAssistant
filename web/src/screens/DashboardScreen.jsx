import { useEffect, useState } from 'react';
import { getTasks, createTask } from '../services/taskService';
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

  return (
    <div style={{ maxWidth: 500, margin: '50px auto' }}>
      <h1>Your Tasks</h1>
      <input value={title} onChange={(e) => setTitle(e.target.value)} placeholder="New task" />
      <button onClick={handleAdd}>Add</button>
      <ul>
        {tasks.map((task) => (
          <li key={task.id}>{task.title}</li>
        ))}
      </ul>
      <button onClick={logout} style={{ float: 'right' }}>Logout</button>
    </div>
  );
}