/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.ReadResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

/**
 *
 * @author Administrator
 */
class PlcImpl implements Plc {

    private int slaveId;
    PlcConfig config;

    public PlcImpl(PlcConfig config) {

        this.config = config;
        this.slaveId = config.getSlaveId();
    }

    @Override
    public boolean getCoil(int offset) throws PlcException {
        boolean[] result = getCoils(offset, 1);
        return result[0];
    }

    @Override
    public boolean[] getCoils(int offset, int count) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            ModbusRequest req = new ReadCoilsRequest(slaveId, offset, count);
            ReadResponse res = (ReadCoilsResponse) master.send(req);

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }
            return res.getBooleanData();

        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }
    }

    @Override
    public void setCoil(int offset, boolean value) throws PlcException {
        setCoils(offset, new boolean[]{value});
    }

    @Override
    public void setCoils(int offset, boolean[] value) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            ModbusRequest req = new WriteCoilsRequest(slaveId, offset, value);
            WriteCoilsResponse res = (WriteCoilsResponse) master.send(req);

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }
    }

    @Override
    public boolean getDiscreteInput(int offset) throws PlcException {
        boolean[] results = getDiscreteInputs(offset, 1);
        return results[0];
    }

    @Override
    public boolean[] getDiscreteInputs(int offset, int count) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            ModbusRequest req = new ReadDiscreteInputsRequest(slaveId, offset, count);
            ReadResponse res = (ReadDiscreteInputsResponse) master.send(req);

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }
            boolean[] result = res.getBooleanData();
            return result;

        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }

    }

    @Override
    public short getRegisterShort(int offset, RegisterType type) throws PlcException {
        short[] result = getRegistersShort(offset, type, 1);
        return result[0];
    }

    @Override
    public short[] getRegistersShort(int offset, RegisterType type, int count) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            ModbusRequest req;
            ReadResponse res;
            if (type == RegisterType.InputRegister) {
                req = new ReadInputRegistersRequest(slaveId, offset, count);
                res = (ReadInputRegistersResponse) master.send(req);
            } else {
                req = new ReadHoldingRegistersRequest(slaveId, offset, count);
                res = (ReadHoldingRegistersResponse) master.send(req);
            }

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }
            short[] data = res.getShortData();
            return data;

        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }
    }

    @Override
    public int getRegisterInt(int offset, RegisterType type) throws PlcException {
        int[] result = getRegistersInt(offset, type, 1);
        return result[0];
    }

    @Override
    public int[] getRegistersInt(int offset, RegisterType type, int count) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            ModbusRequest req;
            ReadResponse res;
            if (type == RegisterType.InputRegister) {
                req = new ReadInputRegistersRequest(slaveId, offset, count * 2);
                res = (ReadInputRegistersResponse) master.send(req);
            } else {
                req = new ReadHoldingRegistersRequest(slaveId, offset, count * 2);
                res = (ReadHoldingRegistersResponse) master.send(req);
            }

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }
            short[] data = res.getShortData();
            return Converter.shortsToInts(data);
        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }

    }

    @Override
    public float getRegisterFloat(int offset, RegisterType type) throws PlcException {
        float[] result = getRegistersFloat(offset, type, 1);
        return result[0];
    }

    @Override
    public float[] getRegistersFloat(int offset, RegisterType type, int count) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            ModbusRequest req;
            ReadResponse res;
            if (type == RegisterType.InputRegister) {
                req = new ReadInputRegistersRequest(slaveId, offset, count * 2);
                res = (ReadInputRegistersResponse) master.send(req);
            } else {
                req = new ReadHoldingRegistersRequest(slaveId, offset, count * 2);
                res = (ReadHoldingRegistersResponse) master.send(req);
            }

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }
            short[] data = res.getShortData();
            return Converter.shortsToFloats(data);

        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }

    }

    @Override
    public void setRegister(int offset, short value) throws PlcException {
        setRegisters(offset, new short[]{value});
    }

    @Override
    public void setRegister(int offset, int value) throws PlcException {
        setRegisters(offset, new int[]{value});
    }

    @Override
    public void setRegister(int offset, float value) throws PlcException {
        setRegisters(offset, new float[]{value});
    }

    @Override
    public void setRegisters(int offset, short[] value) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            ModbusRequest req = new WriteRegistersRequest(slaveId, offset, value);
            WriteRegistersResponse res = (WriteRegistersResponse) master.send(req);

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }
    }

    @Override
    public void setRegisters(int offset, int[] value) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            short[] data = Converter.intsToShorts(value);
            ModbusRequest req = new WriteRegistersRequest(slaveId, offset, data);
            WriteRegistersResponse res = (WriteRegistersResponse) master.send(req);

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }
    }

    @Override
    public void setRegisters(int offset, float[] value) throws PlcException {
        ModbusMaster master = getTcpMaster();
        try {
            master.init();
            short[] data = Converter.floatsToShorts(value);
            ModbusRequest req = new WriteRegistersRequest(slaveId, offset, data);
            WriteRegistersResponse res = (WriteRegistersResponse) master.send(req);

            if (res.isException()) {
                throw new PlcException(String.format("Plc发生异常:[%d] %s", res.getExceptionCode(), res.getExceptionMessage()), null);
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            throw new PlcException("Modbus发生异常:" + ex.getMessage(), ex);
        }
        finally {
            if( master != null){
                master.destroy();
            }
        }
    }

    private ModbusMaster getTcpMaster() {
        ModbusFactory factory = new ModbusFactory();
        IpParameters primaryParams = new IpParameters();
        primaryParams.setHost(config.getIp());
        primaryParams.setPort(config.getPort());
        return factory.createTcpMaster(primaryParams, false);
    }

}
