function fetchSse(url, body, onMessage, onError, onDone) {
  fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  })
    .then((response) => {
      const reader = response.body.getReader();
      const decoder = new TextDecoder();

      const readChunk = () => {
        return reader.read().then(appendChunks);
      };

      const appendChunks = (result) => {
        const chunk = decoder.decode(result.value || new Uint8Array(), {
          stream: !result.done,
        });

        const chunkValue = chunk.toString().replace("data:", "").trim();
        if (chunkValue) {
          onMessage(chunkValue);
        }

        if (!result.done) {
          return readChunk();
        }
      };

      return readChunk();
    })
    .then(() => {
      if (onDone) {
        onDone();
      }
    })
    .catch((e) => {
      if (onError) {
        onError(e);
      }
    });
}
