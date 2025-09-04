// File: src/components/UrlShortener.jsx

import React, { useState } from "react";

export default function UrlShortener() {
  const [longUrl, setLongUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleShorten = async () => {
    if (!longUrl.trim()) {
      setError("Please enter a valid URL");
      return;
    }

    setLoading(true);
    setError("");
    setShortUrl("");

    try {
      const response = await fetch("http://localhost:8080/shorten-url", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(longUrl),
      });

      if (!response.ok) {
        throw new Error("Failed to shorten URL");
      }

      const data = await response.text(); // backend returns string
      setShortUrl(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-50">
      <div className="w-full max-w-md bg-white shadow-xl rounded-2xl p-6">
        <h1 className="text-2xl font-bold mb-4">URL Shortener</h1>
        <p className="text-sm text-gray-600 mb-4">
          Enter a long URL below. The shortened URL will be valid for{" "}
          <strong>7 days</strong> and expire automatically on the{" "}
          <strong>8th day morning at 6 AM</strong>.
        </p>

        <input
          type="text"
          placeholder="Enter your long URL"
          value={longUrl}
          onChange={(e) => setLongUrl(e.target.value)}
          className="w-full border rounded-lg p-2 mb-3 focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <button
          onClick={handleShorten}
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? "Shortening..." : "Shorten URL"}
        </button>

        {error && <p className="text-red-500 text-sm mt-3">{error}</p>}

        {shortUrl && (
          <div className="mt-4 p-3 bg-green-100 rounded-lg">
            <p className="text-sm font-medium text-green-700">
              Shortened URL:
            </p>
            <a
              href={shortUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="text-blue-600 underline break-all"
            >
              {shortUrl}
            </a>
          </div>
        )}
      </div>
    </div>
  );
}
