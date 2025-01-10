import { Terminal } from "@xterm/xterm";

export async function runBinary($path: string, args: string[], t: Terminal) {
  console.log($path, args.join(" "));
  ProcessExecutor.execute($path, args.join(" "));

  globalThis.readProcResp = (data) => {
    console.log(data);
    t.writeln(data.replace(/\n/g, "\r\n").replace(/\r\r\n/g, "\r\n"));
  }

  const resp = t.onData((data) => {
    ProcessExecutor.sendToProcess(data);
  });

  return new Promise((resolve) => {
    globalThis.procEnded = () => {
      resp.dispose();

      resolve(null);
    };
  });
}