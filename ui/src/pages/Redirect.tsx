import { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

function Redirect() {
    const { shortenedKey } = useParams();
    const navigate = useNavigate();
    const serverUrl = import.meta.env.VITE_SERVER_URL;
    const resolveApi = `${serverUrl}/api/urls`;
  
    useEffect(() => {
      const getOriginalUrl = async () => {
        try {
          const response = await fetch(`${resolveApi}/${shortenedKey}`);
          if (response.ok) {
            const data = await response.text();
            window.location.href = data;
          } else {
            console.error("Error resolving shortened URL:", response.statusText);
            navigate("/");
          }
        } catch (error) {
          console.error("Error resolving shortened URL:", error);
          navigate("/");
        }
      };
  
      if (shortenedKey) {
        getOriginalUrl();
      }
    }, [shortenedKey, navigate, resolveApi]);
  
    return (
      <div style={{ textAlign: 'center', marginTop: '20%' }}>
        <h2>Redirecting...</h2>
      </div>
    );
}

export default Redirect;