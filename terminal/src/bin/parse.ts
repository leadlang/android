import { Terminal } from "@xterm/xterm";

export function parse(path: string, terminal: Terminal, cmd: string, args: string[]) {
  switch (cmd) {
    case "ls":
      terminal.writeln(path);
      break;
    case "cdui":
      terminal.writeln("Contacting UI provider...");
      break;
    case "cat":
      terminal.writeln(path)
      break;
    default:
      throw new Error(`Cannot find ${cmd}`)
  }
}