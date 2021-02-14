/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguridad;
/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import bd.Persona;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;
import com.sun.crypto.provider.SunJCE;

public class ManejadorSeguridad {
    private ManejadorSeguridad() {}
    public static void main(String argv[]) throws Exception {
        
        
        
        LlaveServidor llaveServidor = new LlaveServidor(0);
        // Alice encodes her public key, and sends it over to Bob.
        byte[] alicePubKeyEnc = llaveServidor.obtenLlaveInicial();
        
        
        LlaveCliente llaveCliente = new LlaveCliente(alicePubKeyEnc);
        // Bob encodes his public key, and sends it over to Alice.
        byte[] bobPubKeyEnc = llaveCliente.obtenLlave();

        llaveServidor.coordinaConCliente(bobPubKeyEnc);
        llaveCliente.coordinaConServidor();
       
        Persona p = new Persona(1,"Fabian");
        
        System.out.println(p.toString());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(p);

   
        
        byte[] cleartext = out.toByteArray();
        byte[] ciphertext = llaveCliente.encriptaMensaje(cleartext);

        byte[] encodedParams = llaveCliente.obtenParametrosDeCifrado();
        
        byte[] recovered = llaveServidor.decriptaMensaje(ciphertext, encodedParams);
        if (!java.util.Arrays.equals(cleartext, recovered))
            throw new Exception("AES in CBC mode recovered text is " +
                    "different from cleartext");
        System.out.println("AES in CBC mode recovered text is "+
                "same as cleartext");
        System.out.println("\n\nResultado del desencriptado");
        ByteArrayInputStream in = new ByteArrayInputStream(recovered);
        ObjectInputStream is = new ObjectInputStream(in);
        Object res = is.readObject();
        Persona resPersona = (Persona) res;        
        System.out.println(resPersona.toString());

    }

    
}

/*




*/