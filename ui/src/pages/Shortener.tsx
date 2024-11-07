import React, { useState } from 'react';
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "../components/ui/card";
import { Alert, AlertDescription } from "../components/ui/alert";
import { Link } from "lucide-react";

const Home = () => {
    const [longUrl, setLongUrl] = useState('');
    const [shortUrl, setShortUrl] = useState('');
    const [error, setError] = useState('');
    const [copySuccess, setCopySuccess] = useState(false);

    const serverUrl = import.meta.env.VITE_SERVER_URL;
    const shortenApi = `${serverUrl}/api/urls/`;

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');
        setShortUrl('');
        setCopySuccess(false);

        try {
            const response = await fetch(shortenApi, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ url: longUrl }),
            });

            if (!response.ok) {
                throw new Error('Failed to shorten URL');
            }

            const data = await response.text();
            const shortLink = new URL(data, window.location.href);
            setShortUrl(shortLink.href);
        } catch (err) {
            console.error(err);
            setError('An error occurred while shortening the URL');
        }
    };

    const handleCopy = () => {
        navigator.clipboard.writeText(shortUrl);
        setCopySuccess(true);
        setTimeout(() => setCopySuccess(false), 2000); // Reset copy success after 2 seconds
    };

    return (
        <div className="container mx-auto flex justify-center items-center min-h-screen">
            <Card className="w-full max-w-md">
            <CardHeader>
                <CardTitle className="text-2xl font-bold text-center">shrink.it</CardTitle>
            </CardHeader>
            <CardContent>
                <form onSubmit={handleSubmit}>
                <div className="space-y-4">
                    <Input
                    type="url"
                    placeholder="Enter your long URL here"
                    value={longUrl}
                    onChange={(e) => setLongUrl(e.target.value)}
                    required
                    />
                    <Button type="submit" className="w-full">
                    Shrink it!
                    </Button>
                </div>
                </form>
            </CardContent>
            <CardFooter className="flex flex-col items-center">
                {shortUrl && (
                <Alert className="mt-2 w-full flex items-center justify-between">
                    <div className="flex items-center">
                        <Link className="h-4 w-4 mr-2" />
                        <AlertDescription>
                            <a href={shortUrl} target="_blank" rel="noopener noreferrer" className="font-medium underline ml-1">
                                {shortUrl}
                            </a>
                        </AlertDescription>
                    </div>
                    <Button 
                        className="ml-4" 
                        onClick={handleCopy}
                    >
                        {copySuccess ? 'Copied!' : 'Copy'}
                    </Button>
                </Alert>
                )}
                {error && (
                <Alert className="mt-2 w-full bg-red-100 text-red-800">
                    <AlertDescription>{error}</AlertDescription>
                </Alert>
                )}
            </CardFooter>
            </Card>
        </div>
    );
};

export default Home;