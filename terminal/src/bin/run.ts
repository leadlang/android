import { Terminal } from "@xterm/xterm";

export async function runBinary($path: string, args: string[], t: Terminal) {
  console.log($path, args.join(" "));
  ProcessExecutor.execute($path, args.join(" "));

  globalThis.readProcResp = (data) => {
    console.log(`Dat ${data}`);
    t.writeln(data.replace(/\n/g, "\r\n").replace(/\r\r\n/g, "\r\n"));
  }

  const resp = t.onData((data) => {
    //String to Uint8Array
    const dataf = new TextEncoder().encode(data);

    ProcessExecutor.sendToProcess(dataf);
  });

  return new Promise((resolve) => {
    globalThis.procEnded = () => {
      resp.dispose();

      resolve(null);
    };
  });
}