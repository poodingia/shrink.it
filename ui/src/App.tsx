import Home from './pages/Shortener';
import { ThemeProvider } from './components/theme-provider';
import { ModeToggle } from './components/ui/mode-toggle';
import './App.css';
import { Routes, Route, BrowserRouter } from 'react-router-dom';
import NotFound from './pages/NotFound';
import Redirect from './pages/Redirect';

function App() {
  return (
    <ThemeProvider defaultTheme='dark' storageKey='vite-ui-theme'>
      <BrowserRouter>
        <div className="App">
          <div className="mode-toggle-container">
            <ModeToggle />
          </div>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/:shortenedKey" element={<Redirect />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </div>
      </BrowserRouter>
    </ThemeProvider>
  );
}


export default App;
