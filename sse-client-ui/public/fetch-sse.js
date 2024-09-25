function fetchSse(url, body, onMessage, onError, onDone) {
  fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  })
    .then(function (response) {
      var reader = response.body.getReader();
      var decoder = new TextDecoder();

      var readChunk = function () {
        return reader.read().then(appendChunks);
      };

      var appendChunks = function (result) {
        var chunk = decoder.decode(result.value || new Uint8Array(), {
          stream: !result.done,
        });

        var chunkValue = chunk.toString().replace("data: ", "").trim();
        onMessage(chunkValue);

        if (!result.done) {
          return readChunk();
        }
      };

      return readChunk();
    })
    .then(function () {
      if (onDone) {
        onDone();
      }
    })
    .catch(function (e) {
      if (onError) {
        onError(e);
      }
    });
}
